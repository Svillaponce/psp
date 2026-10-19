package org.svillaponce.psp.actividad10;

import java.io.*;
import java.nio.file.*;
import java.util.Locale;

public class Actividad10ProcesadorCsv {
    public static void main(String[] args) {
        if (args.length != 1) { System.err.println("Uso: <fichero.csv>"); System.exit(1); }
        System.exit(procesar(Path.of(args[0])));
    }

    public static int procesar(Path ruta) {
        if (!Files.isRegularFile(ruta) || !Files.isReadable(ruta)) {
            error(0, "FICHERO", "Fichero inexistente o inaccesible"); return 1;
        }
        if (!ruta.toString().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            error(0, "EXTENSION", "Se requiere .csv"); return 2;
        }
        int registros = 0, validos = 0, errores = 0;
        double suma = 0;
        int stockTotal = 0;
        try (BufferedReader reader = Files.newBufferedReader(ruta)) {
            if (!"codigo;nombre;precio;stock".equals(reader.readLine())) {
                error(1, "CABECERA", "Se esperaba codigo;nombre;precio;stock"); return 2;
            }
            String linea;
            while ((linea = reader.readLine()) != null) {
                registros++;
                // -1 conserva campos vacíos al final; sin él un stock vacío desaparecería.
                String[] campos = linea.split(";", -1);
                try {
                    if (campos.length != 4) throw new IllegalArgumentException("Se requieren cuatro campos");
                    for (String campo : campos) if (campo.isBlank()) throw new IllegalArgumentException("Campo obligatorio vacío");
                    double precio;
                    try { precio = Double.parseDouble(campos[2].trim()); }
                    catch (NumberFormatException e) { throw new IllegalArgumentException("Precio no numérico"); }
                    if (!Double.isFinite(precio) || precio <= 0) throw new IllegalArgumentException("Precio debe ser mayor que cero");
                    int stock;
                    try { stock = Integer.parseInt(campos[3].trim()); }
                    catch (NumberFormatException e) { throw new IllegalArgumentException("Stock no entero"); }
                    if (stock < 0) throw new IllegalArgumentException("Stock negativo");
                    suma += precio;
                    stockTotal += stock;
                    validos++;
                } catch (IllegalArgumentException e) {
                    error(registros + 1, "CONTENIDO", e.getMessage()); errores++;
                }
            }
        } catch (IOException e) { error(0, "LECTURA", e.getMessage()); return 1; }
        System.out.println("FICHERO=" + ruta.getFileName());
        System.out.println("REGISTROS=" + registros + "\nVALIDOS=" + validos + "\nERRORES=" + errores);
        System.out.printf(Locale.ROOT, "PRECIO_MEDIO=%.2f%n", validos == 0 ? 0 : suma / validos);
        System.out.println("STOCK_TOTAL=" + stockTotal);
        return errores == 0 ? 0 : 3;
    }

    private static void error(int linea, String tipo, String mensaje) {
        System.err.println("ERROR|" + linea + "|" + tipo + "|" + mensaje);
    }
}
