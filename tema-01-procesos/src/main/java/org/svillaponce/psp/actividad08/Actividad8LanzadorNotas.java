package org.svillaponce.psp.actividad08;

import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Actividad8LanzadorNotas {
    public static void main(String[] args) throws IOException, InterruptedException {
        String ruta;
        if (args.length > 0) ruta = args[0];
        else {
            System.out.print("Ruta de notas: ");
            ruta = new Scanner(System.in).nextLine();
        }
        Path ejecutableJava = Path.of(System.getProperty("java.home"), "bin", "java");
        ProcessBuilder pb = new ProcessBuilder(
                ejecutableJava.toString(), "-cp", System.getProperty("java.class.path"),
                "org.svillaponce.psp.actividad08.Actividad8ConversorNotas", ruta);
        Path carpeta = Path.of("tema-01-procesos", "target", "salidas", "actividad08");
        Files.createDirectories(carpeta);
        Path stdout = carpeta.resolve("salida.txt");
        Path stderr = carpeta.resolve("errores.txt");
        // Redirigir antes de esperar evita que una salida grande bloquee al hijo.
        pb.redirectOutput(stdout.toFile());
        pb.redirectError(stderr.toFile());
        Process proceso = pb.start();
        System.out.println("PID: " + proceso.pid());
        // Para probar el timeout en clase, cambia este valor a 0.
        int limiteSegundos = 5;
        boolean terminado = proceso.waitFor(limiteSegundos, TimeUnit.SECONDS);
        if (!terminado) {
            System.out.println("ESTADO: TIMEOUT");
            proceso.destroy();
            if (!proceso.waitFor(1, TimeUnit.SECONDS)) {
                proceso.destroyForcibly();
                proceso.waitFor();
            }
        }
        int codigo = proceso.exitValue();
        System.out.print(Files.readString(stdout));
        System.err.print(Files.readString(stderr));
        System.out.println("CODIGO: " + codigo);
        boolean generado = terminado && (codigo == 0 || codigo == 3)
                && Files.isRegularFile(Actividad8ConversorNotas.salida(Path.of(ruta)));
        System.out.println("FICHERO GENERADO EN ESTA EJECUCION: " + generado);
    }
}
