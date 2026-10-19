package org.svillaponce.psp.actividad02;

import java.io.*;
import java.nio.file.*;

public class Actividad2DiagnosticoDirectorio {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path ruta = Path.of(args.length == 0 ? "." : args[0]);
        System.out.println("Ruta: " + ruta + " | Absoluta: " + ruta.toAbsolutePath());
        System.out.println("Existe: " + Files.exists(ruta) + " | Directorio: " + Files.isDirectory(ruta)
                + " | Fichero: " + Files.isRegularFile(ruta) + " | Es absoluta: " + ruta.isAbsolute());
        if (!Files.isDirectory(ruta)) { System.err.println("La ruta no existe o no es directorio"); return; }
        File directorio = ruta.toFile(); // Representar una ruta no crea físicamente nada.
        ejecutar(directorio, "pwd");
        ejecutar(directorio, "ls", "-la");
        try (var contenido = Files.list(ruta)) {
            var fichero = contenido.filter(Files::isRegularFile).findFirst();
            if (fichero.isPresent()) ejecutar(directorio, "ls", "-lh", "--", fichero.get().getFileName().toString());
            else System.out.println("No hay ficheros regulares de los que mostrar información.");
        }
    }
    private static void ejecutar(File directorio, String... comando) throws IOException, InterruptedException {
        Process proceso = new ProcessBuilder(comando).directory(directorio).inheritIO().start();
        System.out.println("Código de salida: " + proceso.waitFor());
    }
}
