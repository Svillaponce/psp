package org.svillaponce.psp.actividad07;

import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class Actividad7LanzadorCsv {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.print("Ruta del CSV: ");
        String ruta = new Scanner(System.in).nextLine();
        Path ejecutableJava = Path.of(System.getProperty("java.home"), "bin", "java");
        ProcessBuilder pb = new ProcessBuilder(
                ejecutableJava.toString(), "-cp", System.getProperty("java.class.path"),
                "org.svillaponce.psp.actividad07.Actividad7CorrectorCsv", ruta);

        // Los errores pueden ocupar muchas líneas. Los guardamos en un fichero para
        // que el hijo no se bloquee llenando stderr mientras el padre lee stdout.
        Path carpeta = Path.of("tema-01-procesos", "target", "salidas", "actividad07");
        Files.createDirectories(carpeta);
        Path stdout = carpeta.resolve("salida.txt");
        Path stderr = carpeta.resolve("errores.txt");
        pb.redirectOutput(stdout.toFile());
        pb.redirectError(stderr.toFile());
        Process proceso = pb.start();
        int codigo = proceso.waitFor();
        System.out.println("PID: " + proceso.pid());
        System.out.print(Files.readString(stdout));
        System.err.print(Files.readString(stderr));
        System.out.println("ESTADO: " + (codigo == 0 ? "OK" : "ERROR"));
        System.out.println("CODIGO: " + codigo);
    }
}
