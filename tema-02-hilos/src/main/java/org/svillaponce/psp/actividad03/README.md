# Actividad 3 · Presupuestos de un taller de reparación

Corresponde a las páginas 5–6 del PDF. Dos trabajadores calculan presupuestos. Una pieza sencilla cuesta 8 € y una compleja 14 €. Un importe de hasta 100 €, incluido, se acepta. Una cantidad no positiva provoca un error de tarea.

## Estructura del código

`Actividad3Presupuestos.java` incluye la enumeración `Tipo`, los registros `Solicitud` y `Resultado`, la fábrica de tareas `presupuestar`, el coordinador `ejecutar` y `main`. Se agrupan porque son datos y operaciones pequeños propios de esta actividad, sin dependencias de otros paquetes.

`Tipo` asocia a cada variante su precio entero. `Solicitud` conserva identificador, tipo y cantidad. `Resultado` conserva identificador, importe y decisión. Los registros son inmutables para estos componentes; no se modifican desde los trabajadores.

## Teoría necesaria

Un **pool fijo** reutiliza un número limitado de hilos. `Executors.newFixedThreadPool(2)` permite hasta dos tareas ejecutándose simultáneamente; las demás esperan en la cola interna del ejecutor. Enviar cuatro tareas no crea cuatro trabajadores.

`Callable<Resultado>` representa una tarea cuyo `call()` devuelve un valor y puede lanzar excepciones. `presupuestar` crea ese trabajo como lambda, pero todavía no lo ejecuta. A diferencia de un `Runnable`, permite expresar directamente un resultado de negocio.

`ejecutor.submit(tarea)` **envía** el trabajo y devuelve un `Future<Resultado>`. Un futuro es una referencia al resultado pendiente. `future.get()` **obtiene** el resultado; si todavía no existe, bloquea al coordinador. Si la tarea falló, lanza `ExecutionException`, cuya `getCause()` contiene el fallo original. Si se interrumpe al coordinador que espera, lanza `InterruptedException`, un problema distinto que aquí se propaga.

Se envía el lote completo antes del primer `get`. Enviar una tarea y recogerla inmediatamente dentro del mismo bucle reduciría la concurrencia del lote. Recoger futuros en orden de envío mantiene la salida ordenada, aunque las tareas terminen en otro orden.

`shutdown()` **cierra la admisión** de nuevas tareas y deja terminar las ya enviadas. No espera por sí mismo. `close()` espera la terminación del ejecutor; con este JDK, si esa espera es interrumpida solicita la parada y conserva la interrupción al finalizar. Se invoca en `finally` para cerrar también si el coordinador sale por excepción. El código no necesita más tareas después de enviarlas y por eso llama a `shutdown` antes de recogerlas.

Los contadores y la suma solo los modifica el coordinador. No requieren `synchronized`. Cada trabajador usa su propia solicitud y devuelve su resultado a través del futuro, que también proporciona la visibilidad de memoria necesaria al recuperar el resultado.

## Recorrido de la solución

1. `main` ejecuta un lote con P02=8 y otro con P02=7. Cada lote tiene su propio ejecutor y sus propios contadores.
2. `ejecutar` crea las cuatro solicitudes en el orden del PDF y una lista de futuros en ese mismo orden.
3. Dentro de cada tarea se valida la cantidad. La excepción se lanza **desde la tarea**, de modo que llega a `get` envuelta en `ExecutionException`.
4. Se calcula `precio × cantidad` con `Math.multiplyExact`. Los importes de estos datos son euros enteros; no hace falta `double`. `multiplyExact` también evita aceptar silenciosamente un desbordamiento de entero con cantidades extremas.
5. La tarea devuelve el importe incluso cuando supera el límite. Un presupuesto rechazado es un resultado normal; una cantidad inválida es un error.
6. El coordinador recorre los futuros, imprime cada resultado y cuenta aceptadas o rechazadas. Solo suma los importes aceptados.
7. Si falla una tarea, cuenta un error, imprime su identificador y la causa original y continúa. Así P03 no impide recuperar P04.
8. Se imprime el resumen y se garantiza el cierre del ejecutor.

## Ejecución y resultados

```sh
mvn -pl tema-02-hilos -am compile
java -cp tema-02-hilos/target/classes org.svillaponce.psp.actividad03.Actividad3Presupuestos
```

| Solicitud | Original | Con P02=7 |
|---|---|---|
| P01 | 24 €, aceptada | 24 €, aceptada |
| P02 | 112 €, rechazada | 98 €, aceptada |
| P03 | Error por cantidad 0 | Error por cantidad 0 |
| P04 | 70 €, aceptada | 70 €, aceptada |
| Resumen | 2 aceptadas, 1 rechazada, 1 error; 94 € | 3 aceptadas, 0 rechazadas, 1 error; 192 € |

La [salida de ambas pruebas](salida-pruebas.txt) procede de una ejecución real. Para comprobar la frontera, una solicitud sencilla de 12 unidades da 96 € y una de 13 da 104 €; la condición del código es `importe <= 100`. Una cantidad negativa debe seguir la misma ruta de error que cero. Cambiar el orden de recogida no cambia la regla comercial ni el número máximo de trabajadores.
