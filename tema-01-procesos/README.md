# Unidad 1: actividades de procesos

Correspondencia con **Unidad 1 - Actividades**, de Sebastián Villa Ponce (28/09/2026). Se mantienen los paquetes `org.svillaponce.psp.actividadNN` y los nombres `ActividadN...` usados en el bloque 1. El enunciado usa nombres abreviados: por ejemplo, `Calculadora` corresponde a `Actividad3Calculadora`.

## Comparación con el PDF

| Actividad / páginas | Situación inicial | Resultado y guía |
|---|---|---|
| 1 / 3–4 | Sin paquete | Solo guía para la práctica de terminal, sin programa Java. [Guía](src/main/java/org/svillaponce/psp/actividad01/README.md) |
| 2 / 4–6 | Diagnóstico existente; suponía que había README.md | Selección de un fichero real, carpeta vacía contemplada y códigos de comandos. [Guía](src/main/java/org/svillaponce/psp/actividad02/README.md) |
| 3 / 6–7 | Padre e hijo existentes | Se conservan las operaciones y TRIPLE como ampliación; lanzamiento con Java y classpath del entorno actual. [Guía](src/main/java/org/svillaponce/psp/actividad03/README.md) |
| 4 / 8–10 | Excepción no comprobada; solo validaba formato de producto; protocolo distinto | Exception comprobada, catálogo P101–P103, errores estructurados, decimales estables y captura de ambos canales. [Guía](src/main/java/org/svillaponce/psp/actividad04/README.md) |
| 5 / 10–11 | Comunicación por stdin/stdout implementada | Se conserva BufferedWriter/BufferedReader y flush; classpath heredado y errores visibles. [Guía](src/main/java/org/svillaponce/psp/actividad05/README.md) |
| 6 / 11–14 | Tarea sin nombre y duración fija en supervisor | Nombre y duración por argumentos; demostración 2/8 segundos, plazo de 5 segundos, ProcessHandle y terminación escalonada. [Guía](src/main/java/org/svillaponce/psp/actividad06/README.md) |
| 7 / 14–16 | Sin paquete | Corrector CSV y lanzador con entrada por teclado, estadísticas, errores por línea y códigos 0–3. [Guía](src/main/java/org/svillaponce/psp/actividad07/README.md) |
| 8 / 16–19 | Sin paquete | Conversor parcial de notas, salida hermana, Java seguro, plazo y códigos 0–4. [Guía](src/main/java/org/svillaponce/psp/actividad08/README.md) |
| 9 / 19–22 | Sin paquete | Validación por stdin, sesión de varios usuarios hasta FIN, respuestas individuales y código global. [Guía](src/main/java/org/svillaponce/psp/actividad09/README.md) |
| 10 / 22–27 | Sin paquete | Un hijo por CSV, lanzamiento de todos antes de supervisar, resultados y plazos individuales sin hilos. [Guía](src/main/java/org/svillaponce/psp/actividad10/README.md) |

Cada padre incluye explícitamente su ProcessBuilder, lectura y espera. No existe paquete `util` ni clases `Procesos` o `Captura`. Se repite código para que cada actividad se pueda explicar por separado. El hijo de la actividad 10 incluye su propio corrector.

## Uso

Requiere JDK 26 y Linux para las actividades 1 y 2. Desde la raíz del repositorio:

```sh
mvn compile
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad03.Actividad3LanzadorCalculo CUADRADO 5
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad06.Actividad6SupervisorTareas
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad07.Actividad7LanzadorCsv
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad08.Actividad8LanzadorNotas /tmp/notas.csv
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad09.Actividad9ClienteUsuarios
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad10.Actividad10GestorLotes /tmp/lote
```

Para ensayar cancelaciones cambia a 0 la variable `limiteSegundos` de la actividad 8 o `limiteMillis` de la 10 y recompila. Después restaura sus valores de 5 segundos y 5000 milisegundos.

Los CSV de productos llevan la cabecera exacta `codigo;nombre;precio;stock`. Los de notas no llevan cabecera. Se usa UTF-8, punto decimal y un formato sencillo sin comillas, escape de separadores ni campos multilínea. La media de precios y el stock solo incluyen registros válidos. La conversión escribe directamente en `_convertidas.csv`; las filas incorrectas se omiten. Una cancelación puede dejar un fichero parcial, por lo que también hay que comprobar el estado final del hijo.

La actividad 4 lee directamente los dos canales: el hijo solo produce una línea. En las actividades 7, 8 y 10 se redirigen a ficheros normales dentro de `target/salidas/actividadNN`, antes de iniciar los procesos. Se crean o sobrescriben en cada ejecución y se conservan para poder examinarlos. No se usan ficheros temporales ni gestión automática de capturas. Esta versión de clase supone un padre por actividad ejecutándose cada vez. La actividad 9 alterna petición y respuesta con `flush`.

En el corrector se usan `double` e `int`, sin resolver precisión monetaria ni cantidades extremas. La supervisión del lote se hace por orden para reducir conceptos; el tiempo restante se calcula desde el lanzamiento de cada hijo.

## Verificación realizada

Tras simplificar: compilación correcta con `mvn -o compile`, 40 ejecuciones de comprobación y dos ensayos adicionales del timeout de las actividades 8 y 10 con copias compiladas a límite cero.

Antes de la simplificación se comprobaron 48 ejecuciones reales: las tres operaciones; pedidos válidos y errores de argumentos, catálogo, cantidad y precio; tres textos; CSV válidos e inválidos, extensión y ausencia de entrada; límites de notas y conversión parcial; timeout; códigos de validación de usuarios y sesión persistente; lotes mixtos, vacíos y cancelados; diagnóstico de directorios; tareas rápida y lenta.

`mvn package` no se completó en este entorno: falta el plugin Surefire 2.17 y la caché local de Maven es de solo lectura. No se añadieron dependencias al proyecto.

## Datos reutilizables

La carpeta [datos-prueba](datos-prueba/README.md) contiene CSV válidos e inválidos de productos y notas, extensiones incorrectas, lotes mixtos, sesiones de usuarios. Su guía detalla los resultados esperados.
