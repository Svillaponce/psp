# Actividad 1 · Dos accesos, una entrada

Corresponde a las páginas 2–3 de **Unidad 2 - Actividades** (10/11/2026). Dos puertas leen entradas concurrentemente. Cada código válido solo puede producir una admisión, aunque se presente a la vez en ambas puertas.

## Archivos y responsabilidades

- `RegistroEntradas.java`: contiene los códigos válidos, el conjunto de entradas usadas y los tres contadores. Su enumeración `Resultado` expresa las tres respuestas posibles.
- `Actividad1Accesos.java`: construye el registro, crea una tarea `Runnable` por puerta, arranca los hilos, espera su terminación y muestra el resumen. Su `main` ejecuta las dos pruebas con registros nuevos.

## Teoría necesaria

Un proceso Java puede tener varios **hilos** que comparten objetos en memoria. Cada hilo sigue su propio flujo de ejecución. Concurrencia significa que estos flujos pueden progresar de forma intercalada; no exige que haya dos núcleos ejecutándolos simultáneamente.

`Runnable` describe una tarea mediante `run()`, sin resultado devuelto. Aquí se expresa con una lambda: el bloque recorre la lista de lecturas de una puerta. `new Thread(tarea, nombre)` asocia esa tarea a un hilo y le da un nombre para identificar sus mensajes. `start()` inicia su ejecución independiente. Llamar directamente a `run()` ejecutaría el bloque en el hilo que lo invoca.

El **dato compartido** es el mismo objeto `RegistroEntradas`, especialmente `usadas` y los contadores. Las listas de lecturas pertenecen a cada tarea y son inmutables. Tener un registro distinto por puerta admitiría dos veces un mismo código.

Una **condición de carrera** aparece si el resultado depende de una intercalación incorrecta: Norte consulta que E01 no está usada, Sur consulta lo mismo y ambas la admiten. La operación lógica completa es comprobar la validez y registrar el primer uso, actualizando el contador correspondiente.

Un método de instancia `synchronized` adquiere el monitor de `this`. Solo un hilo puede ejecutar a la vez código protegido por ese mismo monitor. Al salir, incluso por una excepción, libera el monitor. Además, la liberación y posterior adquisición establecen visibilidad de memoria. No basta con proteger únicamente el incremento del contador o con declarar un campo `volatile`: se necesita una operación compuesta indivisible.

`HashSet` evita elementos repetidos pero no es una colección concurrente. Aquí es seguro porque todos sus accesos ocurren bajo el monitor. `add` devuelve `false` cuando el código ya existía. `validas` se crea con `Set.of`, por lo que no cambia durante la ejecución.

`join()` bloquea al coordinador hasta que termina el hilo indicado. Después de un `join` completado, las acciones del hilo son visibles para el coordinador. No convierte en atómicas las operaciones realizadas mientras ambos hilos trabajan; esa es la función de `synchronized`. Se arrancan **ambos** antes de esperar para permitir concurrencia.

## Recorrido del código

1. `ejecutar(false)` crea un registro vacío y las listas exactas del PDF.
2. `puerta` devuelve una tarea que conserva una referencia al registro común y a su lista.
3. Cada iteración llama a `registrar`. Si el código no está en `validas`, incrementa `invalidas`. Si `usadas.add` devuelve `false`, incrementa `duplicadas`. En otro caso incrementa `admitidas`.
4. El método devuelve un `Resultado` y libera el monitor. La tarea imprime puerta, código y resultado fuera de la sección crítica, reduciendo su duración.
5. El coordinador espera ambos hilos y consulta `resumen`, que también usa el monitor para obtener los contadores de manera coherente.
6. `ejecutar(true)` repite con un registro nuevo y añade E02 al final de Norte.

El orden de las líneas no demuestra quién entró primero en el monitor: un hilo puede liberar el monitor y tardar en imprimir. Cada puerta sí conserva el orden de sus lecturas. Para E01 y E03 debe haber exactamente una línea `ADMITIDA` y otra `DUPLICADA`, independientemente de la puerta ganadora.

## Ejecución y pruebas

Desde la raíz del repositorio, con JDK 26:

```sh
mvn -pl tema-02-hilos -am compile
java -cp tema-02-hilos/target/classes org.svillaponce.psp.actividad01.Actividad1Accesos
```

La ejecución incluye ambos escenarios, sin argumentos.

| Escenario | Admitidas | Duplicadas | Inválidas |
|---|---:|---:|---:|
| Original: 8 lecturas | 5 | 2 | 1 |
| E02 adicional: 9 lecturas | 5 | 3 | 1 |

Consulta [salida-pruebas.txt](salida-pruebas.txt) para ver una ejecución real. Repite el programa para observar otras intercalaciones; los totales deben conservarse. Como ejercicio, explica qué ocurriría con dos registros y qué fallo introduce arrancar Norte, esperar su `join` y después arrancar Sur.
