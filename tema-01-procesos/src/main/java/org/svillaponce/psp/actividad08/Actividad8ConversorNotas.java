package org.svillaponce.psp.actividad08;

import java.io.*;
import java.nio.file.*;
import java.util.Locale;

public class Actividad8ConversorNotas {
    public static Path salida(Path entrada) {
        String nombre = entrada.getFileName().toString();
        return entrada.resolveSibling(nombre.substring(0, nombre.length() - 4) + "_convertidas.csv");
    }

    public static void main(String[] args) {
        if (args.length != 1) { System.err.println("Uso: <notas.csv>"); System.exit(1); }
        System.exit(convertir(Path.of(args[0])));
    }

    private static int convertir(Path entrada) {
        if (!Files.isRegularFile(entrada)) { System.err.println("ERROR|FICHERO|No existe"); return 1; }
        if (!entrada.toString().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            System.err.println("ERROR|EXTENSION|Se requiere .csv"); return 2;
        }
        int errores = 0, validos = 0;
        try (BufferedReader reader = Files.newBufferedReader(entrada)) {
            try {
                Path destino = salida(entrada).toAbsolutePath();
                try (BufferedWriter writer = Files.newBufferedWriter(destino)) {
                    String linea; int numero = 0;
                    while ((linea = reader.readLine()) != null) {
                        numero++;
                        try {
                            String[] campos = linea.split(";", -1);
                            if (campos.length != 2) throw new IllegalArgumentException("Estructura incorrecta");
                            if (campos[0].isBlank()) throw new IllegalArgumentException("Alumno vacío");
                            double nota;
                            try { nota = Double.parseDouble(campos[1].trim()); }
                            catch (NumberFormatException e) { throw new IllegalArgumentException("Nota no numérica"); }
                            if (!Double.isFinite(nota) || nota < 0 || nota > 10)
                                throw new IllegalArgumentException("Nota fuera de 0-10");
                            writer.write(campos[0].trim() + ";" + calificacion(nota)); writer.newLine(); validos++;
                        } catch (IllegalArgumentException e) {
                            System.err.println("ERROR|" + numero + "|CONTENIDO|" + e.getMessage()); errores++;
                        }
                    }
                }
                System.out.println("SALIDA=" + destino + "\nVALIDOS=" + validos + "\nERRORES=" + errores);
                return errores == 0 ? 0 : 3;
            } catch (IOException e) { System.err.println("ERROR|E/S|" + e.getMessage()); return 4; }
        } catch (IOException e) { System.err.println("ERROR|LECTURA|" + e.getMessage()); return 1; }
    }

    private static String calificacion(double nota) {
        if (nota < 5) return "SUSPENSO";
        if (nota < 6) return "APROBADO";
        if (nota < 7) return "BIEN";
        if (nota < 9) return "NOTABLE";
        return "SOBRESALIENTE";
    }
}
