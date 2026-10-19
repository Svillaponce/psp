# Actividad 5: Servicio de análisis de texto

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Define tres líneas de respuesta: MAYUSCULAS, LONGITUD y PALABRAS.
2. Implementa el hijo leyendo una línea de stdin, nunca de los argumentos.
3. Calcula los tres datos; decide cómo tratar una entrada vacía y espacios consecutivos.
4. Desde el padre crea el proceso. Envía una línea con `BufferedWriter`, termina la línea y vacía el búfer.
5. Cierra la escritura y lee las respuestas con `BufferedReader`; espera al hijo y consulta su código.

**Pruebas:** «Hola», «Programación de Servicios y Procesos» y «  uno   dos  ». Anota longitud y palabras antes de ejecutar para contrastarlas. Prueba además entrada vacía.

**Cuestiones:** explica por qué el padre escribe con `getOutputStream` y lee con `getInputStream`; qué hacen `flush`, el salto de línea y el cierre; y qué diferencia hay respecto a pasar argumentos.

La longitud es la que devuelve `String.length()` (unidades UTF-16). El número de longitud del ejemplo del PDF debe comprobarse, no copiarse como resultado esperado.

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad05.Actividad5ClienteTexto
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Cómo se lanza el hijo

El padre construye directamente su `ProcessBuilder`: `java.home` permite localizar el ejecutable Java, `-cp` indica dónde buscar las clases y `java.class.path` conserva el classpath con el que arrancaste el padre. El nombre completo de la clase incluye el paquete. Cada argumento ocupa una posición independiente; no se construye una orden de shell.

Configurar el builder todavía no ejecuta nada: `start()` crea el proceso y devuelve un `Process`. `waitFor()` espera su terminación; `exitValue()` consulta su código después de que haya terminado. No hay una clase auxiliar que oculte estos pasos.
