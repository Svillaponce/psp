package org.svillaponce.psp.actividad04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Actividad4GestorPedidos {

    static void main(String[] args)
            throws IOException, InterruptedException {

        if (args.length != 3) {
            System.err.println(
                    "Uso: <codigoProducto> <cantidad> <precio>"
            );
            System.exit(1);
        }

        String classpath = "tema-01-procesos/target/classes";

        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-cp",
                classpath,
                "org.svillaponce.psp.actividad04.Actividad4ProcesadorPedido",
                args[0],
                args[1],
                args[2]
        );

        Process proceso = pb.start();

        System.out.println(
                "PID del proceso hijo: " + proceso.pid()
        );

        String salidaNormal;
        String salidaError;

        try (
                BufferedReader stdout =
                        new BufferedReader(
                                new InputStreamReader(
                                        proceso.getInputStream()
                                )
                        );

                BufferedReader stderr =
                        new BufferedReader(
                                new InputStreamReader(
                                        proceso.getErrorStream()
                                )
                        )
        ) {

            salidaNormal = stdout.readLine();
            salidaError = stderr.readLine();
        }

        int codigoSalida = proceso.waitFor();

        System.out.println();
        System.out.println("===== RESUMEN DEL PROCESO =====");
        System.out.println("PID: " + proceso.pid());
        System.out.println("Código de salida: " + codigoSalida);

        if (codigoSalida == 0) {

            System.out.println("Estado: OK");
            System.out.println(
                    "Respuesta: " + salidaNormal
            );

        } else {

            System.out.println("Estado: ERROR");
            System.out.println(
                    "Detalle: " + salidaError
            );
        }
    }
}