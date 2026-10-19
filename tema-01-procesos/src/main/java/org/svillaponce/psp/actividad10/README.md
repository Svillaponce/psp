# Actividad 10: Procesamiento por lotes

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Investiga `Files.list`, `Files.isRegularFile`, filtrado por extensión, colecciones dinámicas y cierre de streams de directorio.
2. Recibe únicamente el directorio y comprueba que sea válido. Selecciona los CSV regulares del primer nivel; ignora .txt, carpetas y otros ficheros.
3. Guarda las rutas en una `List<Path>` y los hijos en una `List<Process>`. El índice i relaciona el fichero i con su proceso y sus salidas. No se necesitan clases auxiliares.
4. Lanza un hijo por fichero. Guarda también el instante de inicio en una `List<Long>`. Debes lanzar todos antes de empezar a esperar.
5. Cada hijo valida su CSV de productos. La validación se repite dentro de la actividad 10 para poder estudiarla sin depender de otra actividad. El padre no valida contenido.
6. Usa códigos 0 correcto, 1 inaccesible, 2 formato/cabecera y 3 contenido. Emite estadísticas por stdout y errores por stderr.
7. Antes de `start`, redirige los canales a `target/salidas/actividad10/i-salida.txt` e `i-errores.txt`. El número i identifica al hijo. Son ficheros normales, que se crean o sobrescriben; puedes consultarlos después. Evitan que se llenen pipes mientras se supervisa otro hijo. Ejecuta un solo gestor cada vez.
8. Recorre los procesos por orden. Calcula cuánto tiempo ha pasado desde su inicio con `System.nanoTime`; conviértelo a milisegundos con `TimeUnit`. Resta ese tiempo al límite para no conceder otros cinco segundos completos a cada hijo. `Math.max(0, restante)` evita tiempos negativos.
9. Usa `waitFor` con el tiempo restante. Si no termina, llama a `destroy`, espera un segundo y, si hace falta, usa `destroyForcibly` y confirma con `waitFor`.
10. Lee las salidas, consulta el código y acumula correctos, errores y timeout. Muestra PID y estado mediante `ProcessHandle` y construye el resumen final.

El bloque `finally` detiene cualquier hijo que siga vivo si el padre falla. Se ha elegido una supervisión por orden para facilitar la explicación: un hijo posterior puede ser detenido algo después de su plazo mientras se termina de tratar el anterior.

**Pruebas:** directorio inválido; sin CSV; lote correcto; lote mixto y ruta con espacios. Para provocar timeout cambia `limiteMillis` de 5000 a 0, recompila y restaura después el valor. Compara total con correctos + errores + timeout.

**Restricción:** no uses Thread, ExecutorService ni CompletableFuture. La simultaneidad la dan los procesos del sistema operativo.

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad10.Actividad10GestorLotes
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Ficheros para practicar

Dispones de entradas permanentes válidas e inválidas en [datos-prueba](../../../../../../../datos-prueba/README.md), con resultados esperados y comandos para repetir las pruebas.

## Cómo se lanza el hijo

El padre construye directamente su `ProcessBuilder`: `java.home` permite localizar el ejecutable Java, `-cp` indica dónde buscar las clases y `java.class.path` conserva el classpath con el que arrancaste el padre. El nombre completo de la clase incluye el paquete. Cada argumento ocupa una posición independiente; no se construye una orden de shell.

Configurar el builder todavía no ejecuta nada: `start()` crea el proceso y devuelve un `Process`. `waitFor()` espera su terminación; `exitValue()` consulta su código después de que haya terminado. No hay una clase auxiliar que oculte estos pasos.
