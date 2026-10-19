# Datos permanentes para pruebas manuales

Estos ficheros se conservan en el proyecto y se pueden reutilizar al rehacer el código. Las rutas de los ejemplos parten de la raíz del repositorio. No edites los originales si quieres repetir las pruebas: copia el fichero antes de modificarlo.

## Actividad 7: productos

Ejecuta el padre e introduce por teclado una ruta de la tabla:

```sh
mvn compile
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad07.Actividad7LanzadorCsv
```

Por ejemplo: `tema-01-procesos/datos-prueba/productos/valido.csv`.

Los códigos de la tabla corresponden al **hijo**, que el padre muestra en su resumen. El padre no propaga necesariamente ese código al terminal.

| Fichero en `productos/` | Código | Resultado esperado |
|---|---:|---|
| `valido.csv` | 0 | 2 registros, 2 válidos, media 15.00, stock 5 |
| `valido-acentos.csv` | 0 | 2 válidos, media 25.00, stock 4; UTF-8 y stock cero |
| `solo-cabecera.csv` | 0 | 0 registros, media 0.00 y stock 0 |
| `extension-mayuscula.CSV` | 0 | Extensión aceptada sin distinguir mayúsculas |
| `vacio.csv` | 3 | Falta cabecera |
| `cabecera-incorrecta.csv` | 3 | Cabecera distinta de la requerida |
| `menos-campos.csv` | 3 | Tres campos en vez de cuatro |
| `mas-campos.csv` | 3 | Cinco campos |
| `campos-vacios.csv` | 3 | Código, nombre y stock vacíos en distintas filas |
| `precio-no-numerico.csv` | 3 | Precio abc |
| `precio-cero-negativo.csv` | 3 | Dos precios no positivos |
| `precio-especial.csv` | 3 | NaN e Infinity no son precios válidos |
| `precio-coma.csv` | 3 | Se requiere punto decimal |
| `stock-negativo.csv` | 3 | Stock menor que cero |
| `stock-no-entero.csv` | 3 | Stock abc y 1.5 |
| `varios-errores.csv` | 3 | 5 registros, 2 válidos, 3 errores, media 15.00, stock 5 |
| `linea-vacia.csv` | 3 | La línea vacía también cuenta como registro incorrecto |
| `separador-incorrecto.csv` | 3 | Comas en lugar de punto y coma |
| `extension-incorrecta.txt` | 2 | Contenido CSV válido pero extensión incorrecta |
| `extension-incorrecta.json` | 2 | Tipo de fichero incorrecto |

Para el código **1**, introduce `tema-01-procesos/datos-prueba/no-existe.csv`. Esa ruta se deja deliberadamente sin crear. Puedes introducir también la ruta de un directorio para comprobar que no se acepta como fichero.

## Actividad 8: notas

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad08.Actividad8LanzadorNotas tema-01-procesos/datos-prueba/notas/mixtas.csv
```

| Fichero en `notas/` | Código del hijo | Resultado esperado |
|---|---:|---|
| `validas.csv` | 0 | NOTABLE, APROBADO, SUSPENSO, SOBRESALIENTE |
| `limites.csv` | 0 | Comprueba los extremos de los cinco intervalos, incluidos 0 y 10 |
| `nota-no-numerica.csv` | 3 | Tres errores, salida vacía |
| `fuera-de-rango.csv` | 3 | Dos errores, salida vacía |
| `estructura-incorrecta.csv` | 3 | Dos errores de estructura |
| `alumno-vacio.csv` | 3 | Dos identificadores vacíos |
| `mixtas.csv` | 3 | Solo ALU001;NOTABLE, ALU003;SUSPENSO y ALU005;SOBRESALIENTE en la salida; tres errores |
| `vacio.csv` | 0 | Conversión sin registros, salida vacía |
| `extension-incorrecta.txt` | 2 | No genera conversión |

La salida se crea al lado de la entrada con el sufijo `_convertidas.csv`. Está excluida de Git y puede borrarse para repetir la práctica. La ruta `notas/no-existe.csv` permite comprobar el código 1.

Para provocar el código 4 de escritura de forma reproducible, copia `validas.csv` a una carpeta de trabajo y crea allí **un directorio** llamado `validas_convertidas.csv`. El directorio debe contener un fichero para impedir que sea reemplazado. Ejecuta la conversión de esa copia; no prepares esta colisión sobre los datos originales.

## Actividad 10: lotes

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad10.Actividad10GestorLotes tema-01-procesos/datos-prueba/lote-mixto
```

| Directorio | Resultado esperado |
|---|---|
| `lote-correcto` | Total 2, correctos 2, errores 0 |
| `lote-mixto` | Total 3, correcto 1, errores 2: uno de contenido y otro de formato |
| `lote-sin-csv` | Total 0 |
| `ruta con espacios` | Un CSV correcto; pasa la ruta entre comillas en el terminal |

En `lote-mixto` se deben ignorar el TXT, el JSON y el **directorio** `subdirectorio.csv`. No se exploran subdirectorios. Puedes pasar `productos/valido.csv` como argumento del gestor para comprobar que rechaza una ruta que no sea directorio.

Un caso de fichero inaccesible requiere cambiar permisos en una copia o retirar el fichero entre el descubrimiento y su apertura. No se incluyen permisos especiales en estos datos, ya que dependen del sistema y del usuario que ejecute Java.

## Timeout

No necesitas generar ficheros grandes ni usar otro lenguaje. Para ensayar la cancelación cambia `limiteSegundos` a 0 en la actividad 8 o `limiteMillis` a 0 en la actividad 10. Recompila y ejecuta con cualquiera de los ejemplos. Restaura después los valores normales. El resultado será TIMEOUT si el hijo sigue activo cuando se consulta.

Las salidas de diagnóstico se conservan en `tema-01-procesos/target/salidas/actividadNN`. Se sobrescriben al ejecutar otra vez; en el lote puede haber salidas antiguas de índices que no se usen en la ejecución actual. El resumen de la consola indica los ficheros de esta ejecución. Una conversión cancelada puede dejar una salida `_convertidas.csv` parcial.

## Actividades 2 y 9

Para la actividad 2 usa este directorio, `ruta con espacios`, un fichero de `productos/` y la ruta inexistente. Prueba rutas absolutas y relativas.

Los TXT de `usuarios/` contienen sesiones para la actividad 9. Puedes copiar sus líneas en la consola o redirigir la entrada:

```sh
java -cp tema-01-procesos/target/classes org.svillaponce.psp.actividad09.Actividad9ClienteUsuarios < tema-01-procesos/datos-prueba/usuarios/sesion.txt
```

En `sesion.txt` se esperan un usuario válido, un error de dominio y otro de edad; código global del hijo 4 (primer error). En `limites-y-errores.txt`, dos válidos y cinco errores; código global 2. Ambos terminan con FIN.
