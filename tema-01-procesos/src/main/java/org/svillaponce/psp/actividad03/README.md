# Actividad 3: Servicio de cálculo externo

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Define el contrato del hijo: operación y número por argumentos, respuesta `RESULTADO=valor` por stdout.
2. Implementa y prueba primero la calculadora independiente con CUADRADO, CUBO y DOBLE.
3. En el padre construye el comando de Java con el classpath y el nombre completo de la clase hija; pasa cada argumento por separado.
4. Inicia el proceso y muestra su PID. Lee stdout con un lector de líneas y espera el código de salida.
5. Presenta el resultado recibido y muestra los errores del hijo. El padre no calcula el resultado.

**Pruebas:** CUADRADO 5 → 25; CUBO 3 → 27; DOBLE 8 → 16. Añade operación desconocida, número no válido y argumentos incompletos.

**Cuestiones:** identifica padre e hijo; explica la independencia de memoria y compara crear un proceso con llamar a un método. TRIPLE es una ampliación opcional de la solución previa.

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad03.Actividad3LanzadorCalculo
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Cómo se lanza el hijo

El padre construye directamente su `ProcessBuilder`: `java.home` permite localizar el ejecutable Java, `-cp` indica dónde buscar las clases y `java.class.path` conserva el classpath con el que arrancaste el padre. El nombre completo de la clase incluye el paquete. Cada argumento ocupa una posición independiente; no se construye una orden de shell.

Configurar el builder todavía no ejecuta nada: `start()` crea el proceso y devuelve un `Process`. `waitFor()` espera su terminación; `exitValue()` consulta su código después de que haya terminado. No hay una clase auxiliar que oculte estos pasos.
