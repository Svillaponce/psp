package org.svillaponce.psp.actividad06;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class Actividad6SupervisorTareas {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            supervisar("TAREA_RAPIDA", "2");
            supervisar("TAREA_LENTA", "8");
        } else if (args.length == 2) supervisar(args[0], args[1]);
        else System.err.println("Uso: [nombre segundos]");
    }
    private static void supervisar(String nombre, String segundos) throws Exception {
        Path ejecutableJava = Path.of(System.getProperty("java.home"), "bin", "java");
        ProcessBuilder pb = new ProcessBuilder(
                ejecutableJava.toString(), "-cp", System.getProperty("java.class.path"),
                "org.svillaponce.psp.actividad06.Actividad6TareaLenta", nombre, segundos);
        Process proceso = pb.inheritIO().start();
        try {
            ProcessHandle handle = proceso.toHandle();
            System.out.println("TAREA: " + nombre + " | PID: " + handle.pid() + " | VIVO: " + proceso.isAlive());
            handle.parent().ifPresent(p -> System.out.println("PPID: " + p.pid()));
            System.out.println("INFO: " + handle.info());
            if (proceso.waitFor(5, TimeUnit.SECONDS)) {
                System.out.println("RESULTADO: " + (proceso.exitValue() == 0 ? "COMPLETADA" : "ERROR"));
            } else {
                System.out.println("RESULTADO: TIMEOUT | ACCION: Finalización solicitada");
                proceso.destroy();
                if (!proceso.waitFor(1, TimeUnit.SECONDS)) {
                    proceso.destroyForcibly();
                    proceso.waitFor();
                }
            }
            System.out.println("CODIGO: " + proceso.exitValue() + " | ESTADO FINAL: " + (proceso.isAlive() ? "ACTIVO" : "TERMINADO"));
        } finally {
            if (proceso.isAlive()) {
                proceso.destroy();
                if (!proceso.waitFor(1, TimeUnit.SECONDS)) {
                    proceso.destroyForcibly();
                    proceso.waitFor();
                }
            }
        }
    }
}
