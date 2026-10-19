# Actividad 6: Supervisor de tareas

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Diseña un hijo que reciba nombre y segundos, anuncie el inicio, espere y anuncie su finalización.
2. Valida argumentos y duración; diferencia un fallo de entrada de una tarea completada.
3. En el padre muestra PID y `isAlive`. Usa `ProcessHandle` para consultar padre e información disponible sin asumir que siempre existe.
4. Espera como máximo cinco segundos. Si termina, recoge el código y distingue éxito y error.
5. Si agota el plazo, anuncia TIMEOUT, solicita `destroy`, espera brevemente y emplea `destroyForcibly` si sigue vivo. Confirma la terminación.
6. Imprime estado final y código incluso tras una cancelación.

**Pruebas:** TAREA_RAPIDA 2 y TAREA_LENTA 8. Añade duración negativa y no numérica. La ejecución sin argumentos del supervisor demuestra ambas tareas.

**Cuestiones:** ¿por qué intentar primero `destroy`? ¿Qué diferencia `isAlive` de `waitFor`? ¿Qué añade `ProcessHandle`? ¿Qué sucede con una espera sin límite si el hijo nunca termina?

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad06.Actividad6SupervisorTareas
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Cómo se lanza el hijo

El padre construye directamente su `ProcessBuilder`: `java.home` permite localizar el ejecutable Java, `-cp` indica dónde buscar las clases y `java.class.path` conserva el classpath con el que arrancaste el padre. El nombre completo de la clase incluye el paquete. Cada argumento ocupa una posición independiente; no se construye una orden de shell.

Configurar el builder todavía no ejecuta nada: `start()` crea el proceso y devuelve un `Process`. `waitFor()` espera su terminación; `exitValue()` consulta su código después de que haya terminado. No hay una clase auxiliar que oculte estos pasos.
