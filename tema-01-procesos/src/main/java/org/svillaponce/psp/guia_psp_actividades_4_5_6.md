# Chuleta docente — PSP Actividades 4, 5 y 6

> Guía breve para seguir en clase. No es teoría completa.

## Actividad 4 — Procesador de pedidos con errores

### Qué trabajamos
- Proceso padre + proceso hijo.
- `stdout` para resultados correctos.
- `stderr` para errores.
- Códigos de salida distintos según el error.
- Excepción personalizada + `enum`.
- Lectura de `getInputStream()`, `getErrorStream()` y `waitFor()`.

### Pasos en clase
1. Crear `Actividad4TipoError`, `Actividad4ProcesamientoException`, `Actividad4ProcesadorPedido` y `Actividad4GestorPedidos`.
2. Empezar por el **hijo**.
3. Validar argumentos, código de producto, cantidad y precio.
4. Caso correcto: escribir por `System.out` y terminar con código `0`.
5. Caso de error: escribir por `System.err` y devolver un código de salida distinto.
6. Probar directamente el hijo con:
   - `P103 4 12.50`
   - producto incorrecto
   - cantidad `0`
   - precio negativo
   - cantidad no numérica
7. Crear el **padre**.
8. Lanzar el hijo con `ProcessBuilder`.
9. Leer:
   - `proceso.getInputStream()` → `stdout` del hijo.
   - `proceso.getErrorStream()` → `stderr` del hijo.
10. Usar `waitFor()` y mostrar un resumen con el código de salida.

### Novedades
- Separación real entre salida normal y salida de error.
- Uso de códigos de finalización como parte del protocolo.
- Excepción personalizada para clasificar errores.
- Un stream vacío puede bloquear una lectura mientras el proceso siga vivo.
- Los buffers de los pipes son finitos: si el hijo genera mucha salida y el padre no la consume, puede bloquearse.

---

## Actividad 5 — Servicio de análisis de texto

### Qué trabajamos
- Comunicación **padre → hijo** mediante `stdin`.
- Comunicación **hijo → padre** mediante `stdout`.
- `BufferedWriter` para enviar datos.
- `BufferedReader` para recibir respuestas.
- `flush()` para forzar el envío.
- `java.lang.IO` para entrada por teclado del padre.
- Ruta absoluta del ejecutable Java para no depender de `PATH`.

### Pasos en clase
1. Crear `Actividad5AnalizadorTexto` y `Actividad5ClienteTexto`.
2. Programar primero el **hijo**.
3. El hijo lee una línea desde `System.in`.
4. Calcula mayúsculas, longitud y número de palabras.
5. Devuelve los resultados mediante `System.out`.
6. Probar el hijo directamente escribiendo una línea en consola.
7. Crear el **padre**.
8. Obtener Java sin depender del `PATH`:
   ```java
   Path javaPath = Path.of(
       System.getProperty("java.home"),
       "bin",
       "java"
   );
   ```
9. Leer el texto del usuario con:
   ```java
   String texto = readln("Introduce un texto: ");
   ```
10. Enviar al hijo mediante `proceso.getOutputStream()`.
11. Escribir la línea, hacer `newLine()` y `flush()`.
12. Leer las respuestas mediante `proceso.getInputStream()`.
13. Recorrer todas las líneas hasta `null`.
14. Esperar al hijo con `waitFor()`.

### Novedades
- `getOutputStream()` del `Process` escribe en el **stdin del hijo**.
- `getInputStream()` del `Process` lee el **stdout del hijo**.
- `flush()` evita que los datos se queden en el buffer.
- Cerrar el `BufferedWriter` indica que no habrá más datos.
- El padre usa `Logger` para sus mensajes.
- El hijo conserva `System.out` / `System.err` porque forman parte de la comunicación entre procesos.

---

## Actividad 6 — Supervisor de tareas externas

### Qué trabajamos
- Espera con tiempo máximo.
- Comprobación del estado de un proceso.
- Terminación normal y forzada.
- `ProcessHandle`.
- `inheritIO()`.

### Pasos en clase
1. Crear `Actividad6TareaLenta` y `Actividad6SupervisorTareas`.
2. El hijo recibe una duración en segundos.
3. Simular una tarea con:
   ```java
   Thread.sleep(segundos * 1000L);
   ```
   Solo se usa para simular tiempo de trabajo; todavía no estamos estudiando hilos.
4. En el padre, lanzar al hijo con `ProcessBuilder`.
5. Usar:
   ```java
   pb.inheritIO();
   ```
   para compartir la terminal con el hijo.
6. Obtener:
   ```java
   ProcessHandle handle = proceso.toHandle();
   ```
7. Mostrar PID del hijo y PID del padre.
8. Comprobar:
   ```java
   proceso.isAlive()
   ```
9. Primera prueba:
   - tarea: `3 s`
   - timeout: `5 s`
   - debe terminar normalmente.
10. Segunda prueba:
    - tarea: `7 s`
    - timeout: `5 s`
    - `waitFor(5, TimeUnit.SECONDS)` devuelve `false`.
11. Antes de destruir, comprobar que sigue vivo.
12. Ejecutar:
    ```java
    proceso.destroy();
    ```
13. Esperar un pequeño margen:
    ```java
    proceso.waitFor(2, TimeUnit.SECONDS);
    ```
14. Si aún sigue vivo:
    ```java
    proceso.destroyForcibly();
    ```
15. Comprobar al final:
    ```java
    proceso.isAlive()
    ```

### Qué explicar
- `waitFor()` normal puede esperar indefinidamente.
- `waitFor(timeout, unidad)` devuelve `true` si termina y `false` si sigue vivo.
- `destroy()` solicita terminar el proceso.
- `destroyForcibly()` fuerza su terminación si no responde.
- `ProcessHandle` permite inspeccionar el proceso y su relación padre-hijo.
- `inheritIO()` evita gestionar manualmente los streams cuando solo queremos ver la salida en terminal.

### Resultado observado
Con tarea de `7 s` y timeout de `5 s`:
- el hijo estaba vivo antes de `destroy()`;
- `destroy()` fue suficiente;
- al finalizar `isAlive()` devolvió `false`;
- no fue necesario ejecutar `destroyForcibly()`.

---

## Resumen rápido

| Actividad | Idea principal |
|---|---|
| 4 | `stdout`, `stderr` y códigos de salida |
| 5 | comunicación bidireccional mediante streams |
| 6 | supervisión, timeout y control del ciclo de vida |

```text
Ej. 4 → controlar cómo termina un proceso
Ej. 5 → comunicarse con un proceso
Ej. 6 → supervisar y detener un proceso
```
