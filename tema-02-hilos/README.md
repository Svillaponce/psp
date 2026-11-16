# Unidad 2 · Hilos y programación concurrente

Soluciones de **Unidad 2 - Actividades**, Sebastián Villa Ponce, IES Salvador Gadea, 10/11/2026. Se sigue la estructura del tema 1: paquetes `org.svillaponce.psp.actividadNN`, clases de entrada `ActividadN...` y un `README.md` dentro de cada paquete. Cada ejercicio es independiente; el ejemplo previo de clase permanece disponible.

| Enunciado | Paquete y guía | Clase de entrada |
|---|---|---|
| Actividad 1, páginas 2–3 | [actividad01](src/main/java/org/svillaponce/psp/actividad01/README.md) | `Actividad1Accesos` |
| Actividad 2, páginas 3–4 | [actividad02](src/main/java/org/svillaponce/psp/actividad02/README.md) | `Actividad2Laboratorio` |
| Actividad 3, páginas 5–6 | [actividad03](src/main/java/org/svillaponce/psp/actividad03/README.md) | `Actividad3Presupuestos` |
| Integrador, páginas 6–7 | [problema_integrador](src/main/java/org/svillaponce/psp/problema_integrador/README.md) | `ProblemaIntegradorCentralPedidos` |

## Compilar y ejecutar

Se requiere JDK 26, como establece el `pom.xml` del módulo. No se añaden dependencias. Desde la raíz:

```sh
mvn -pl tema-02-hilos -am compile
java -cp tema-02-hilos/target/classes org.svillaponce.psp.actividad01.Actividad1Accesos
java -cp tema-02-hilos/target/classes org.svillaponce.psp.actividad02.Actividad2Laboratorio
java -cp tema-02-hilos/target/classes org.svillaponce.psp.actividad03.Actividad3Presupuestos
java -cp tema-02-hilos/target/classes org.svillaponce.psp.problema_integrador.ProblemaIntegradorCentralPedidos
```

En IntelliJ se puede ejecutar el `main` de cada clase. Cada programa realiza automáticamente las dos pruebas de su enunciado, usando datos nuevos entre ellas. Los archivos `salida-pruebas.txt` de cada paquete conservan una ejecución real de ambas pruebas. Los repartos entre hilos pueden variar; los totales exigidos son estables.

Cada guía explica responsabilidades, estado compartido, conceptos teóricos, recorrido del código, comandos, resultados esperados y límites de la solución. Los ejemplos emplean `main` clásico para facilitar la ejecución por consola. El integrador implementa además la compensación de reservas ante una interrupción detectada durante la preparación.

## Verificación realizada

Se compilaron todas las clases del módulo, incluido el ejemplo previo, con `javac --release 26 -encoding UTF-8`. Se ejecutó cada programa 15 veces (60 ejecuciones, dos escenarios por ejecución), comprobando los resúmenes, la admisión única de E01 y E03 y el procesamiento único de cada muestra. Se guardó una salida de cada programa en su paquete.

Una comprobación adicional verificó la devolución de una reserva cuando la preparación detecta una interrupción, la conservación del indicador de interrupción, el rechazo sin descuento y las excepciones por cantidad cero y producto desconocido. `git diff --check` no detectó problemas de espacios. Los comandos Maven anteriores son instrucciones de uso; no se ejecutaron porque Maven no estaba disponible en el PATH del entorno de verificación.
