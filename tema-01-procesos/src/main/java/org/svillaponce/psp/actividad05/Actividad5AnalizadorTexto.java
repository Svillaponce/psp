package org.svillaponce.psp.actividad05;

import static java.lang.IO.readln;

public class Actividad5AnalizadorTexto {

    static void main() {
        String texto = readln();

        if (texto == null || texto.isBlank()) {
            System.err.println(
                    "ERROR: No se ha recibido ningún texto."
            );
            System.exit(1);
        }

        int longitud = texto.length();

        int palabras =
                texto.trim()
                        .split("\\s+")
                        .length;

        System.out.println(
                "MAYUSCULAS=" + texto.toUpperCase()
        );

        System.out.println(
                "LONGITUD=" + longitud
        );

        System.out.println(
                "PALABRAS=" + palabras
        );
    }
}