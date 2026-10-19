# Actividad 1: radiografía de procesos en Linux

Esta actividad se trabaja en la terminal, sin solución Java en este paquete.

1. Ejecuta `ps`, `ps -f`, `ps -ef`, `pstree -p` y `top`.
2. Anota el PID de la shell y el PID y PPID de `ps`. Localiza su relación en el árbol.
3. Ejecuta desde la terminal un programa Java que ya tengas y que permanezca activo, como el cliente de la actividad 9 mientras espera entrada. Localízalo desde otra terminal.
4. Observa PID, proceso padre, estado y consumo de CPU. Ejecuta una segunda instancia y compara.

Explica PID frente a PPID, por qué `ps` aparece en su propio listado, qué aporta el árbol y por qué no consumir CPU no implica haber terminado. Relaciona estas observaciones con el scheduler.
