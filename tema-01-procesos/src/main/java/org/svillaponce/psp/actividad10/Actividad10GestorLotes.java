package org.svillaponce.psp.actividad10;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Actividad10GestorLotes {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 1) {
            System.err.println("Uso: <directorio>");
            return;
        }
        Path directorio = Path.of(args[0]);
        if (!Files.isDirectory(directorio)) {
            System.err.println("No existe o no es directorio");
            return;
        }
        List<Path> ficheros = new ArrayList<>();
        try (DirectoryStream<Path> contenido = Files.newDirectoryStream(directorio)) {
            for (Path ruta : contenido) {
                if (Files.isRegularFile(ruta) && ruta.toString().toLowerCase(Locale.ROOT).endsWith(".csv")) {
                    ficheros.add(ruta);
                }
            }
        }
        System.out.println("CSV encontrados: " + ficheros.size());
        Path carpeta = Path.of("tema-01-procesos", "target", "salidas", "actividad10");
        Files.createDirectories(carpeta);
        Path ejecutableJava = Path.of(System.getProperty("java.home"), "bin", "java");
        // El mismo índice relaciona fichero, proceso e instante de inicio.
        List<Process> procesos = new ArrayList<>();
        List<Long> inicios = new ArrayList<>();
        List<String> resultados = new ArrayList<>();
        int correctos = 0;
        int errores = 0;
        int timeouts = 0;
        // Para ensayar cancelaciones, cambia este valor a 0.
        long limiteMillis = 5000;
        try {
            for (int i = 0; i < ficheros.size(); i++) {
                ProcessBuilder pb = new ProcessBuilder(
                        ejecutableJava.toString(), "-cp", System.getProperty("java.class.path"),
                        "org.svillaponce.psp.actividad10.Actividad10ProcesadorCsv", ficheros.get(i).toString());
                // Un par de ficheros por hijo: así no se mezclan sus resultados ni se llenan pipes.
                pb.redirectOutput(carpeta.resolve(i + "-salida.txt").toFile());
                pb.redirectError(carpeta.resolve(i + "-errores.txt").toFile());
                procesos.add(pb.start());
                inicios.add(System.nanoTime());
            }
            // Todos los hijos ya están lanzados antes de empezar a esperar.
            for (int i = 0; i < procesos.size(); i++) {
                Process proceso = procesos.get(i);
                ProcessHandle handle = proceso.toHandle();
                System.out.println("FICHERO: " + ficheros.get(i).getFileName());
                System.out.println("PID: " + handle.pid() + " | ACTIVO: " + handle.isAlive());
                handle.parent().ifPresent(p -> System.out.println("PPID: " + p.pid()));
                // nanoTime mide intervalos sin depender de ajustes del reloj del sistema.
                // Restamos lo ya transcurrido: cada hijo tiene su plazo desde el lanzamiento.
                long transcurrido = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicios.get(i));
                long restante = Math.max(0, limiteMillis - transcurrido);
                boolean terminado = proceso.waitFor(restante, TimeUnit.MILLISECONDS);
                String estado;
                if (!terminado) {
                    timeouts++;
                    estado = "TIMEOUT";
                    proceso.destroy();
                    if (!proceso.waitFor(1, TimeUnit.SECONDS)) {
                        proceso.destroyForcibly();
                        proceso.waitFor();
                    }
                } else {
                    int codigo = proceso.exitValue();
                    if (codigo == 0) correctos++; else errores++;
                    estado = switch (codigo) {
                        case 0 -> "OK";
                        case 1 -> "FICHERO INACCESIBLE";
                        case 2 -> "FORMATO INCORRECTO";
                        case 3 -> "ERROR DE CONTENIDO";
                        default -> "ERROR INESPERADO";
                    };
                }
                System.out.print(Files.readString(carpeta.resolve(i + "-salida.txt")));
                System.err.print(Files.readString(carpeta.resolve(i + "-errores.txt")));
                System.out.println("CODIGO: " + proceso.exitValue());
                resultados.add(ficheros.get(i).getFileName() + " → " + estado);
            }
        } finally {
            // Si se interrumpe el padre o falla una operación, no dejar hijos trabajando.
            for (Process proceso : procesos) {
                if (proceso.isAlive()) {
                    proceso.destroy();
                    if (!proceso.waitFor(1, TimeUnit.SECONDS)) {
                        proceso.destroyForcibly();
                        proceso.waitFor();
                    }
                }
            }
        }
        System.out.println("===== RESULTADO DEL LOTE =====");
        for (String resultado : resultados) System.out.println(resultado);
        System.out.printf("Total: %d%nCorrectos: %d%nCon errores: %d%nTimeout: %d%n",
                ficheros.size(), correctos, errores, timeouts);
    }
}
