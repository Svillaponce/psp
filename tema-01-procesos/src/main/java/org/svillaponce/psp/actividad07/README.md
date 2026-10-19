# Actividad 7: Corrector de ficheros CSV

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Escribe el contrato del fichero: cabecera `codigo;nombre;precio;stock`, separador punto y coma, sin comillas ni campos multilínea.
2. Separa responsabilidades: el padre recibe la ruta por teclado; el hijo valida y calcula.
3. Comprueba acceso y extensión antes de leer. Valida la cabecera y recorre los registros conservando los campos vacíos finales.
4. Valida cuatro campos obligatorios, precio numérico positivo y stock entero no negativo. Conserva el número de línea física (cabecera = 1).
5. Continúa tras los errores. Acumula registros totales, válidos, suma de precios y stock de los válidos; evita dividir por cero.
6. Devuelve resumen por stdout y errores `ERROR|linea|tipo|descripcion` por stderr. Usa códigos 0 correcto, 1 inaccesible, 2 extensión y 3 contenido.
7. En el padre crea `target/salidas/actividad07`, redirige stdout a `salida.txt` y stderr a `errores.txt` con `redirectOutput` y `redirectError`. Inicia, espera con `waitFor`, lee ambos ficheros con `Files.readString` y muestra PID, estado y código.

La redirección se configura antes de `start()`. Los ficheros se crean o sobrescriben al lanzar el hijo. Son salidas normales, no temporales: puedes abrirlas tras ejecutar. Se usan porque una lista larga de errores podría llenar un pipe y bloquear al hijo si el padre no lo leyera. Para simplificar, se admite una ejecución del padre cada vez.

**Pruebas mínimas:** fichero correcto con dos productos de precios 10 y 20 (media 15); inexistente; extensión .txt; fila con tres campos; precio abc; stock -1; varias filas incorrectas. Añade cabecera errónea, campo final vacío y fichero solo con cabecera.

**Decisiones:** la media y el stock solo incluyen filas válidas. Se admite un fichero con cabecera y cero productos. Para facilitar el trabajo en clase se usan `double` para precios e `int` para stock, sin abordar precisión monetaria ni cantidades extremas.

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad07.Actividad7LanzadorCsv
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Ficheros para practicar

Dispones de entradas permanentes válidas e inválidas en [datos-prueba](../../../../../../../datos-prueba/README.md), con resultados esperados y comandos para repetir las pruebas.

## Cómo se lanza el hijo

El padre construye directamente su `ProcessBuilder`: `java.home` permite localizar el ejecutable Java, `-cp` indica dónde buscar las clases y `java.class.path` conserva el classpath con el que arrancaste el padre. El nombre completo de la clase incluye el paquete. Cada argumento ocupa una posición independiente; no se construye una orden de shell.

Configurar el builder todavía no ejecuta nada: `start()` crea el proceso y devuelve un `Process`. `waitFor()` espera su terminación; `exitValue()` consulta su código después de que haya terminado. No hay una clase auxiliar que oculte estos pasos.
