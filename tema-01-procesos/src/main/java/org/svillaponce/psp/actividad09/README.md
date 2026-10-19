# Actividad 9: Servicio de validación de usuarios

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Define una petición por línea `nombre;email;edad` y una respuesta `OK|...` o `ERROR|tipo|mensaje`.
2. Implementa primero un hijo que lea una petición desde stdin. Comprueba tres campos, nombre, formato del correo, dominio permitido y edad entera entre 16 y 100.
3. Permite los dominios centro.es, educacion.es y alumno.es. Define códigos 0 correcto, 1 datos incompletos, 2 nombre, 3 correo, 4 dominio y 5 edad.
4. Construye un padre que solicite datos y los envíe con salto de línea y `flush`. La validación corresponde exclusivamente al hijo.
5. Amplía el hijo a un bucle: procesa una línea, responde y vacía su salida. FIN o EOF terminan la sesión.
6. En el padre alterna envío y lectura: no envíes todo un lote grande antes de leer respuestas. Cierra los flujos y consulta el código global al finalizar.

**Pruebas:** Laura Pérez;laura@centro.es;24, Mario López;mario@gmail.com;20 y Ana Ruiz;ana@alumno.es;abc en la misma sesión, seguida de FIN. Añade nombre vacío, correo sin @, edad 15, 16, 100 y 101, y estructura incompleta. Comprueba que se mantiene un solo PID.

**Decisiones:** todas las respuestas del protocolo, incluidos errores de validación, van por stdout para mantener una respuesta por petición. Stderr queda para fallos del programa. El código global conserva el primer error; cada línea mantiene su resultado individual. El protocolo no permite punto y coma en los campos ni `|` en el nombre.

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad09.Actividad9ClienteUsuarios
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Ficheros para practicar

Dispones de entradas permanentes válidas e inválidas en [datos-prueba](../../../../../../../datos-prueba/README.md), con resultados esperados y comandos para repetir las pruebas.

## Cómo se lanza el hijo

El padre construye directamente su `ProcessBuilder`: `java.home` permite localizar el ejecutable Java, `-cp` indica dónde buscar las clases y `java.class.path` conserva el classpath con el que arrancaste el padre. El nombre completo de la clase incluye el paquete. Cada argumento ocupa una posición independiente; no se construye una orden de shell.

Configurar el builder todavía no ejecuta nada: `start()` crea el proceso y devuelve un `Process`. `waitFor()` espera su terminación; `exitValue()` consulta su código después de que haya terminado. No hay una clase auxiliar que oculte estos pasos.
