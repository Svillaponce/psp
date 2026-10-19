package org.svillaponce.psp.actividad09;

import java.nio.file.Path;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class Actividad9ClienteUsuarios {
    public static void main(String[] args) throws Exception {
        Path ejecutableJava = Path.of(System.getProperty("java.home"), "bin", "java");
        ProcessBuilder pb = new ProcessBuilder(
                ejecutableJava.toString(), "-cp", System.getProperty("java.class.path"),
                "org.svillaponce.psp.actividad09.Actividad9ValidadorUsuarios");
        Process proceso = pb.redirectError(ProcessBuilder.Redirect.INHERIT).start();
        System.out.println("PID: " + proceso.pid() + " | Introduce nombre;email;edad, o FIN");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(proceso.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream(), StandardCharsets.UTF_8))) {
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            String linea;
            while ((linea = teclado.readLine()) != null && !linea.equals("FIN")) {
                writer.write(linea);
                writer.newLine();
                writer.flush();
                String respuesta = reader.readLine();
                if (respuesta == null) throw new EOFException("El hijo cerró el protocolo sin responder");
                System.out.println("Respuesta: " + respuesta);
            }
            writer.write("FIN");
            writer.newLine();
            writer.flush();
        } finally {
            if (!proceso.waitFor(2, TimeUnit.SECONDS)) {
                proceso.destroy();
                if (!proceso.waitFor(1, TimeUnit.SECONDS)) {
                    proceso.destroyForcibly();
                    proceso.waitFor();
                }
            }
        }
        System.out.println("CODIGO GLOBAL: " + proceso.exitValue());
    }
}
