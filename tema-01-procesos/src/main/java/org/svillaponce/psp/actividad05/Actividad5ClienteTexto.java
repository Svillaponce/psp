package org.svillaponce.psp.actividad05;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.lang.IO.readln;

public class Actividad5ClienteTexto {

    private static final Logger LOGGER =
            Logger.getLogger(Actividad5ClienteTexto.class.getName());

    static void main()
            throws IOException, InterruptedException {

        Path javaPath = Path.of(
                System.getProperty("java.home"),
                "bin",
                "java"
        );

        String classpath =
                "tema-01-procesos/target/classes";

        ProcessBuilder pb = new ProcessBuilder(
                javaPath.toString(),
                "-cp",
                classpath,
                "org.svillaponce.psp.actividad05.Actividad5AnalizadorTexto"
        );

        Process proceso = pb.start();

        LOGGER.info(
                () -> "PID del proceso hijo: " + proceso.pid()
        );

        String texto = readln("Introduce un texto: ");

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new OutputStreamWriter(
                                     proceso.getOutputStream(),
                                     StandardCharsets.UTF_8
                             )
                     )) {

            writer.write(texto);
            writer.newLine();
            writer.flush();
        }

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     proceso.getInputStream(),
                                     StandardCharsets.UTF_8
                             )
                     )) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                LOGGER.info("Respuesta: " + linea);
            }
        }

        int codigoSalida = proceso.waitFor();

        LOGGER.info(
                () -> "Código de salida: " + codigoSalida
        );
    }
}