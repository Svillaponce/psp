# Actividad 8: Conversor de notas

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Define entrada sin cabecera: identificador y nota separados por punto y coma. La salida hermana añade `_convertidas` al nombre.
2. Implementa el hijo leyendo todas las líneas y acumulando los errores sin detenerse al primero.
3. Valida estructura, identificador, nota numérica y rango inclusivo 0–10. Convierte mediante umbrales 5, 6, 7 y 9.
4. Genera el CSV solo con las filas válidas. Escribe directamente con `Files.newBufferedWriter` en el fichero de destino: se crea si no existe o se vacía si ya existía. Es más sencillo para clase; si el proceso se cancela puede quedar una salida parcial.
5. Define códigos: 0 correcto, 1 entrada inaccesible, 2 extensión, 3 salida con registros incorrectos y 4 error de E/S durante conversión/publicación.
6. El padre localiza Java dentro de `java.home`, fija un plazo y redirige las salidas a `target/salidas/actividad08/salida.txt` y `errores.txt`. Estos ficheros se crean o sobrescriben al lanzar; se leen después de terminar. Así no se llenan pipes mientras espera con timeout. Si se excede, solicita y confirma la terminación con escalado si hace falta.
7. Comprueba el resultado de esta ejecución y que exista el fichero final. Un resultado antiguo no acredita que la conversión actual haya terminado.

**Pruebas:** fichero correcto, inexistente, .txt, nota abc, -1 y 11, fila con tres campos y mezcla de válidos e inválidos. Prueba los límites 0, 4.99, 5, 6, 7, 9 y 10. Contrasta el contenido del fichero generado, no solo el código.

Para comprobar el timeout cambia `limiteSegundos` de 5 a 0 en el padre y recompila. Restaura después el valor. El fichero convertido puede quedar parcial si hay cancelación: solo se considera completado si el hijo termina con código 0 o 3. Para simplificar, ejecuta un solo padre cada vez.

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad08.Actividad8LanzadorNotas
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Ficheros para practicar

Dispones de entradas permanentes válidas e inválidas en [datos-prueba](../../../../../../../datos-prueba/README.md), con resultados esperados y comandos para repetir las pruebas.

## Cómo se lanza el hijo

El padre construye directamente su `ProcessBuilder`: `java.home` permite localizar el ejecutable Java, `-cp` indica dónde buscar las clases y `java.class.path` conserva el classpath con el que arrancaste el padre. El nombre completo de la clase incluye el paquete. Cada argumento ocupa una posición independiente; no se construye una orden de shell.

Configurar el builder todavía no ejecuta nada: `start()` crea el proceso y devuelve un `Process`. `waitFor()` espera su terminación; `exitValue()` consulta su código después de que haya terminado. No hay una clase auxiliar que oculte estos pasos.
