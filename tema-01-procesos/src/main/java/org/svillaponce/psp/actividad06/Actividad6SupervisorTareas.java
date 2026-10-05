package org.svillaponce.psp.actividad06;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Actividad6SupervisorTareas {

    private static final Logger LOGGER =
            Logger.getLogger(Actividad6SupervisorTareas.class.getName());

    static void main()
            throws IOException, InterruptedException {

        Path javaPath = Path.of(
                System.getProperty("java.home"),
                "bin",
                "java"
        );

        String classpath =
                "tema-01-procesos/target/classes";

        ProcessBuilder pb = new ProcessBuilder(
                javaPath.toString(),
                "-cp",
                classpath,
                "org.svillaponce.psp.actividad06.Actividad6TareaLenta",
                "3"
        );

        pb.inheritIO();

        Process proceso = pb.start();

        ProcessHandle handle = proceso.toHandle();

        LOGGER.info(
                () -> "Proceso iniciado. PID: "
                        + proceso.pid()
        );

        handle.parent().ifPresent(
                padre -> LOGGER.info(
                        () -> "PID del padre: " + padre.pid()
                )
        );

        LOGGER.info(() -> "¿Proceso vivo?: " + proceso.isAlive());

        boolean terminado = proceso.waitFor(5, TimeUnit.SECONDS);

        if (terminado) {
            LOGGER.info(() -> "Proceso terminado. Código: " + proceso.exitValue());
            LOGGER.info(() -> "¿Proceso vivo al finalizar?: " + proceso.isAlive());
        } else {
            LOGGER.warning("Tiempo máximo superado.");
            LOGGER.info(() -> "¿Proceso vivo antes de destroy()?: " + proceso.isAlive());
            proceso.destroy();
            boolean detenido = proceso.waitFor(2, TimeUnit.SECONDS);

            if (!detenido) {
                LOGGER.warning("No responde a destroy(). Se fuerza la terminación.");
                proceso.destroyForcibly();
                proceso.waitFor();
            }
            LOGGER.info(() -> "¿Proceso vivo al finalizar?: " + proceso.isAlive());
        }
    }
}