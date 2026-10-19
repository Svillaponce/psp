# Actividad 4: Pedidos con errores detallados

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Define el catálogo de productos (para las pruebas: P101, P102 y P103), los tipos de error y sus códigos de salida.
2. Crea una excepción comprobada que extienda `Exception` y guarde tipo y mensaje.
3. En el hijo valida número de argumentos, existencia del producto, formato y signo de cantidad y precio. No confundas tener formato P999 con existir en el catálogo.
4. Calcula el total solo con un pedido válido. Emite `OK|producto|cantidad|precio|total` por stdout y `ERROR|tipo|mensaje` por stderr si falla.
5. Mantén el punto decimal independientemente de la configuración regional.
6. En el padre lanza el hijo y recoge ambos canales, PID y código. Presenta estado y todos los datos devueltos.

**Pruebas:** P103 4 12.50 → total 50.00; faltan argumentos; P999; cantidad abc, 0 y -2; precio abc, 0 y -1. Comprueba también NaN e Infinity, que no representan precios válidos.

**Cuestiones:** ¿Qué aporta una excepción con tipo? ¿Por qué stderr y código de salida se complementan? ¿Por qué leer dos pipes grandes de manera secuencial puede bloquear al padre? En esta actividad el hijo emite una sola línea, por lo que se leen stdout y stderr directamente con `BufferedReader`. Esta simplificación depende del tamaño reducido de la respuesta.

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad04.Actividad4GestorPedidos
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Cómo se lanza el hijo

El padre construye directamente su `ProcessBuilder`: `java.home` permite localizar el ejecutable Java, `-cp` indica dónde buscar las clases y `java.class.path` conserva el classpath con el que arrancaste el padre. El nombre completo de la clase incluye el paquete. Cada argumento ocupa una posición independiente; no se construye una orden de shell.

Configurar el builder todavía no ejecuta nada: `start()` crea el proceso y devuelve un `Process`. `waitFor()` espera su terminación; `exitValue()` consulta su código después de que haya terminado. No hay una clase auxiliar que oculte estos pasos.
