# Actividad 2 · Clasificación de muestras en un laboratorio

Corresponde a las páginas 3–4 del PDF. Un productor deposita cinco muestras y dos consumidores las clasifican. La mesa admite dos muestras **pendientes**; las que ya están clasificando los técnicos no ocupan hueco en ella.

## Archivos y responsabilidades

- `Muestra.java`: `record` inmutable con identificador y prioridad. Java genera constructor, accesores `id()` y `prioridad()`, igualdad y representación textual.
- `Actividad2Laboratorio.java`: define la señal de fin, construye las tareas y ejecuta el lote completo y la cancelación con mesa vacía.

## Teoría necesaria

El patrón **productor-consumidor** desacopla quién genera los datos de quién los procesa. Una cola acotada limita el trabajo pendiente y aplica contrapresión: el productor espera si genera más rápido de lo que se consume.

`BlockingQueue<Muestra>` es la interfaz y `ArrayBlockingQueue<>(2)` la implementación utilizada. Su capacidad es fija. Sus operaciones son seguras entre hilos, por lo que no hay que añadir `synchronized` alrededor de `put` y `take`.

- `put(muestra)` inserta o bloquea al productor cuando los dos huecos están ocupados. Continúa cuando un consumidor libera espacio.
- `take()` retira la primera muestra o bloquea al consumidor mientras la cola esté vacía. Continúa cuando el productor añade un elemento.
- Ambas operaciones son interrumpibles: pueden lanzar `InterruptedException`.

La cola es **FIFO**: retira según el orden de inserción. La prioridad es un atributo informativo; una muestra urgente no adelanta a una normal. FIFO tampoco garantiza el orden de finalización ni el orden de impresión con dos técnicos. Uno puede retirar M01, quedar desprogramado y registrar su clasificación después de M02.

`FIN` es un objeto especial compartido, también llamado centinela. Se compara por identidad (`==`) porque se introduce exactamente esa instancia. Cada consumidor se retira al recibirlo. Hay que insertar **dos** señales: si solo se enviara una, el otro técnico podría quedar esperando indefinidamente. Se insertan después del lote, conservando el orden FIFO.

Una **interrupción** es una solicitud cooperativa. No mata arbitrariamente el hilo. Un `take` bloqueado o un `sleep` responde lanzando `InterruptedException`; si la solicitud llega antes de `take`, la operación también la detecta. La excepción limpia el indicador, por lo que el `catch` lo restablece con `Thread.currentThread().interrupt()` antes de registrar el motivo y terminar. No hay espera activa ni un bucle que ignore la cancelación.

## Recorrido del código

1. `loteCompleto` crea una cola nueva de capacidad dos.
2. `tecnico` crea un hilo con nombre. Su bucle retira una muestra, comprueba el centinela y simula la clasificación con una pausa de 30 ms. La pausa ocurre **después** de retirar: recepción ya puede usar el hueco libre.
3. La tarea de recepción recorre M01 normal, M02 urgente, M03 normal, M04 urgente y M05 normal. Solo este productor introduce muestras.
4. Recepción envía las dos señales de fin. Si recepción fuese interrumpida, solicita la interrupción de ambos técnicos para que no esperen señales que ya no se enviarán.
5. Se arrancan los tres hilos antes de los `join`. El coordinador espera a recepción y a los dos técnicos.
6. `cancelarConMesaVacia` crea otra cola vacía, arranca dos técnicos sin productor y solicita desde `main` su interrupción. Los dos `join` confirman que han terminado antes de que el método retorne.

La prueba de cancelación no necesita una pausa para acertar un instante: funciona tanto si los técnicos ya esperan en `take` como si todavía van a entrar en él. No se exige observar el estado `WAITING`; se comprueba su salida por interrupción.

## Ejecución y pruebas

```sh
mvn -pl tema-02-hilos -am compile
java -cp tema-02-hilos/target/classes org.svillaponce.psp.actividad02.Actividad2Laboratorio
```

| Escenario | Muestras procesadas | Salidas por FIN | Salidas por interrupción |
|---|---:|---:|---:|
| Lote completo | 5, cada identificador una vez | 2 | 0 |
| Cola vacía | 0 | 0 | 2 |

La [salida real](salida-pruebas.txt) registra ambas pruebas. El reparto entre técnicos varía. La pausa simula trabajo, no sincroniza el acceso a la cola. Esta actividad termina inmediatamente al cancelar: si se interrumpiera durante una clasificación, la muestra retirada no se reinsertaría. Una política de reintento requeriría distinguir trabajo pendiente, en curso y confirmado.

Para razonar desde cero: dibuja dos casillas de mesa y una muestra en manos de cada técnico. Explica por qué pueden coexistir cuatro muestras en el sistema aunque la capacidad de la cola sea dos, y por qué una cola vacía no significa necesariamente que toda clasificación haya terminado.
