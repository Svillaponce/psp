package org.svillaponce.psp.actividad05;

import java.nio.file.Path;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static java.lang.IO.readln;

public class Actividad5ClienteTexto {

    private static final Logger LOGGER =
            Logger.getLogger(Actividad5ClienteTexto.class.getName());

    static void main()
            throws IOException, InterruptedException {

        Path ejecutableJava = Path.of(System.getProperty("java.home"), "bin", "java");
        ProcessBuilder pb = new ProcessBuilder(
                ejecutableJava.toString(), "-cp", System.getProperty("java.class.path"),
                "org.svillaponce.psp.actividad05.Actividad5AnalizadorTexto");
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process proceso = pb.start();

        LOGGER.info(
                () -> "PID del proceso hijo: " + proceso.pid()
        );

        String texto = readln("Introduce un texto: ");

        if (texto == null) { proceso.destroy(); return; }

        // OutputStream del padre conecta con stdin del hijo. El salto y flush desbloquean readLine().
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