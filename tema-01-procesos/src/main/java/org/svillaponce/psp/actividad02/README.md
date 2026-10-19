# Actividad 2: Diagnóstico de directorios

Guía para resolver el enunciado desde cero en clase. Trabaja en un proyecto vacío o crea las clases sin consultar las soluciones de este paquete.

## Desarrollo

1. Diseña una tabla para existencia, directorio, fichero y tipo de ruta.
2. Recibe una ruta y represéntala con `Path`; consulta sus propiedades con `Files`.
3. Detén el diagnóstico si no es un directorio válido, explicando el motivo.
4. Convierte el `Path` a `File` y úsalo en `ProcessBuilder.directory`.
5. Ejecuta `pwd` y `ls -la`. Localiza un fichero real del directorio y pide información con `ls -lh`. Decide qué informar cuando no hay ficheros.
6. Muestra salida, errores y código de cada comando; espera su terminación.

**Pruebas:** directorio existente, inexistente, fichero, ruta relativa y absoluta; añade una carpeta vacía y una ruta con espacios.

**Cuestiones:** ¿`Path.of` y `new File` crean algo físicamente? ¿Cuándo se detecta un directorio de trabajo inexistente: al configurar o al iniciar? Distingue representar una ruta de crearla.

## Ejecución y comprobación

El proyecto utiliza JDK 26. Desde la raíz, compila con `mvn compile`. La clase de entrada de esta actividad se ejecuta con:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad02.Actividad2DiagnosticoDirectorio
```

Añade los argumentos descritos arriba cuando correspondan. Las clases hijas también pueden ejecutarse por separado usando su nombre completo.

Antes de programar, prepara una tabla con entrada, stdout esperado, stderr esperado y código de salida. Después registra los resultados reales. Explica con tus palabras el recorrido de los datos entre los procesos y justifica cada decisión.

## Ficheros para practicar

Dispones de entradas permanentes válidas e inválidas en [datos-prueba](../../../../../../../datos-prueba/README.md), con resultados esperados y comandos para repetir las pruebas.
