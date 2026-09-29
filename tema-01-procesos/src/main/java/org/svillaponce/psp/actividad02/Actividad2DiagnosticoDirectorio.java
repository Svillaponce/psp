package org.svillaponce.psp.actividad02;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Actividad2DiagnosticoDirectorio {

    static void main(String[] args) throws IOException {

        Path ruta;

        if (args.length > 0) {
            ruta = Path.of(args[0]);
        } else {
            ruta = Path.of(".");
        }

        System.out.println("Ruta introducida: " + ruta);
        System.out.println("Ruta absoluta: " + ruta.toAbsolutePath());
        System.out.println("Nombre final: " + ruta.getFileName());
        System.out.println("Directorio padre: " + ruta.getParent());

        System.out.println("¿Existe?: " + Files.exists(ruta));
        System.out.println("¿Es directorio?: " + Files.isDirectory(ruta));
        System.out.println("¿Es fichero?: " + Files.isRegularFile(ruta));
        System.out.println("¿Es absoluta?: " + ruta.isAbsolute());

        File directorio = ruta.toFile();
        if (!Files.exists(ruta)) {
            System.err.println("ERROR: La ruta no existe.");
            return;
        }

        if (!Files.isDirectory(ruta)) {
            System.err.println("ERROR: La ruta no es un directorio.");
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("pwd");
        pb.directory(directorio);

        Process proceso = pb.start();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(proceso.getInputStream()))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                System.out.println("Proceso devuelve: " + linea);
            }
        }

        System.out.println("\nContenido del directorio:");

        ProcessBuilder pbLs = new ProcessBuilder("ls", "-la");
        pbLs.directory(directorio);

        Process procesoLs = pbLs.start();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(procesoLs.getInputStream()))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }

        System.out.println("\nInformación de un fichero:");

        ProcessBuilder pbLsLh = new ProcessBuilder("ls", "-lh", "README.md");
        pbLsLh.directory(directorio);

        Process procesoLsLh = pbLsLh.start();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(procesoLsLh.getInputStream()))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }

    }
}
