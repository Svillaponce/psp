# Problema integrador · Central de pedidos con inventario compartido

Corresponde a las páginas 6–7 del PDF. Tres trabajadores procesan seis pedidos sobre un único inventario. Un pedido reserva todas sus unidades o se rechaza sin modificar existencias. Los datos inválidos provocan excepciones.

## Clases y responsabilidades

- `Pedido`: registro inmutable con identificador, producto y unidades.
- `Inventario`: posee el mapa de existencias. Valida y reserva bajo un monitor, devuelve reservas y facilita una copia inmutable para consultar el stock.
- `TareaPedido`: `Callable` que reserva, prepara fuera del monitor y devuelve un `Resultado` con identificador y decisión. También compensa una preparación interrumpida.
- `ProblemaIntegradorCentralPedidos`: crea el inventario y el pool, envía todo el lote, recoge resultados, cuenta estados, muestra existencias y cierra el ejecutor. `main` ejecuta los escenarios TEC=6 y TEC=8 con inventarios independientes.

## Teoría: recurso compartido y operación atómica

El recurso compartido es el mismo objeto `Inventario`, al que apuntan las seis tareas. Su `HashMap` es mutable y requiere protección. Las solicitudes y los resultados son datos independientes e inmutables.

La operación que hay que hacer indivisible es **consultar disponibilidad y descontar las unidades**. Con TEC=6, si dos pedidos de cuatro unidades consultaran simultáneamente el seis antes de descontar, ambos podrían vender un total de ocho unidades. Proteger solo el `put`, o usar un mapa concurrente manteniendo una secuencia separada de consulta y escritura, no resolvería esa carrera.

`reservar` es `synchronized`: utiliza el monitor de ese inventario durante la validación, consulta y posible descuento. Si faltan unidades devuelve `false` antes de escribir. No realiza descuentos parciales. Las cantidades no positivas o los productos desconocidos lanzan `IllegalArgumentException` antes de modificar el mapa.

La preparación utiliza `Thread.sleep(40)` **fuera** de `reservar`. Dormir dentro del método sincronizado retendría el monitor: los otros trabajadores no podrían reservar ni productos distintos. La pausa solo representa trabajo; no garantiza un orden de adquisición del monitor. El monitor único simplifica el ejemplo y serializa reservas, pero la preparación sigue siendo concurrente.

`consultarStock` y `devolver` usan el mismo monitor. `consultarStock` devuelve `Map.copyOf(stock)`, impidiendo que quien consulta modifique el mapa original por fuera de la protección.

## Teoría: tareas, futuros y coordinación

`newFixedThreadPool(3)` limita a tres las tareas simultáneas. `submit(new TareaPedido(...))` envía cada trabajo y devuelve su `Future`. Se envían los seis antes de recoger ninguno.

`get()` espera el resultado de un futuro y proporciona la visibilidad del trabajo completado. Las tareas pueden acabar en otro orden, aunque el coordinador imprime por orden de envío. Una respuesta `completado=false` indica falta de stock, un rechazo comercial. `ExecutionException` indica que la tarea lanzó una excepción; el coordinador imprime `getCause()` y sigue recogiendo los demás pedidos.

Solo el coordinador actualiza los contadores. No necesita bloqueos para ellos. El inventario sí necesita protección mientras las tareas se ejecutan. Cuando todos los `get` han retornado o informado del fallo de sus tareas, se imprime el resumen y la copia del stock. El bloque `finally` llama a `shutdown` y `close`: deja de admitir trabajo y espera la terminación, incluso si se abandona la recogida por una excepción.

## Recorrido de un pedido

1. `call()` pide la reserva. Si los datos son inválidos, la excepción sale de la tarea y será recogida por el futuro.
2. Si no hay stock, devuelve inmediatamente un rechazo. No inicia preparación y no hay unidades que devolver.
3. Si se reservó, crea la marca local `completado=false` y entra en `try`.
4. Simula la preparación con `sleep`. Después comprueba el indicador de interrupción para detectar también una solicitud recibida justo al terminar la pausa.
5. Construye el resultado y marca `completado=true`, que es el punto de confirmación local. Devuelve el pedido completado.
6. Si la pausa o la comprobación lanza `InterruptedException`, el `catch` restablece el indicador y propaga la excepción. El `finally` se ejecuta antes de salir.
7. Si no se confirmó el pedido, `finally` devuelve las unidades mediante una operación sincronizada. El pedido no queda completado y el futuro informa del fallo.

## Decisión sobre interrupciones y devolución de stock

La reserva pertenece a una sola instancia de tarea y se intenta una vez. Solo existe **un punto de devolución**, el `finally` de esa tarea, y solo se alcanza tras una reserva exitosa. El coordinador no devuelve unidades al recibir un error. Así se evita la devolución doble. También se devuelve si surge otra excepción durante la preparación antes de la confirmación.

La política de este ejemplo es devolver cuando la interrupción se detecta antes de confirmar. Una interrupción recibida después de la confirmación no revierte el pedido. No se puede convertir una comprobación del indicador en una transacción con cualquier cancelación externa: `Future.cancel(true)` solicita interrupción y puede marcar el futuro como cancelado antes de que la tarea termine su limpieza. Si se añadiera cancelación al coordinador, habría que tratar `CancellationException`, esperar la terminación real y consultar un estado de pedido explícito antes de publicar el stock definitivo.

Para una aplicación persistente con reintentos, varias instancias o fallos de proceso, usaría un identificador de reserva y estados como RESERVADO, CONFIRMADO y DEVUELTO, cambiados atómicamente. Solo la transición RESERVADO→DEVUELTO sumaría unidades, haciendo la compensación idempotente. El `finally` actual resuelve la interrupción cooperativa dentro de esta JVM; no es un mecanismo de recuperación tras apagar el proceso.

## Ejecución y pruebas del PDF

```sh
mvn -pl tema-02-hilos -am compile
java -cp tema-02-hilos/target/classes org.svillaponce.psp.problema_integrador.ProblemaIntegradorCentralPedidos
```

| Stock inicial | Completados | Rechazados | Errores | Stock final |
|---|---:|---:|---:|---|
| TEC=6, RAT=5 | 3 | 1 | 2 | TEC=2, RAT=0 |
| TEC=8, RAT=5 | 4 | 0 | 2 | TEC=0, RAT=0 |

Con seis teclados, solo uno de PED-01 y PED-02 puede completarse; cualquiera puede ganar. PED-03 y PED-04 consumen conjuntamente los cinco ratones. PED-05 falla por cantidad cero y PED-06 por producto XXX. Con ocho teclados se completan ambos pedidos de TEC. La [salida real](salida-pruebas.txt) conserva ambas ejecuciones.

Para revisar la solución, verifica que ningún saldo es negativo, que un rechazo conserva el stock y que cada pedido aparece exactamente una vez. Para ensayar una interrupción en preparación, ejecuta `TareaPedido.call()` en un hilo con su indicador de interrupción activado: `sleep` falla después de reservar, el `finally` devuelve las unidades y el stock debe coincidir con el inicial. No devuelvas desde otro lugar para “asegurar” la compensación: duplicaría el abono.
