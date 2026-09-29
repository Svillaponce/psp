package org.svillaponce.psp.actividad03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Actividad3LanzadorCalculo {

    static void main(String[] args) throws IOException, InterruptedException {

        if (args.length != 2) {
            System.err.println("Uso: <operacion válida> <numero>");
            System.exit(1);
        }

        String operacion = args[0];
        String numero = args[1];

        String classpath = "tema-01-procesos/target/classes";

        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-cp",
                classpath,
                "org.svillaponce.psp.actividad03.Actividad3Calculadora",
                operacion,
                numero
        );

        Process proceso = pb.start();

        System.out.println("PID del proceso hijo: " + proceso.pid());

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(proceso.getInputStream()))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                System.out.println("Salida recibida: " + linea);
            }
        }

        int codigoSalida = proceso.waitFor();

        System.out.println("Código de salida: " + codigoSalida);
    }
}
