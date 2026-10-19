package org.svillaponce.psp.actividad04;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Actividad4GestorPedidos {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path ejecutableJava = Path.of(System.getProperty("java.home"), "bin", "java");
        List<String> comando = new ArrayList<>();
        comando.add(ejecutableJava.toString());
        comando.add("-cp");
        comando.add(System.getProperty("java.class.path"));
        comando.add("org.svillaponce.psp.actividad04.Actividad4ProcesadorPedido");
        // Se envían incluso argumentos incompletos: la validación corresponde al hijo.
        for (String argumento : args) comando.add(argumento);
        Process proceso = new ProcessBuilder(comando).start();
        System.out.println("PID: " + proceso.pid());

        // Este hijo devuelve una única línea en uno de los dos canales.
        // Por eso aquí podemos leerlos por orden; no sirve para salidas de tamaño arbitrario.
        try (BufferedReader stdout = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
             BufferedReader stderr = new BufferedReader(new InputStreamReader(proceso.getErrorStream()))) {
            String respuesta = stdout.readLine();
            String error = stderr.readLine();
            int codigo = proceso.waitFor();
            System.out.println("ESTADO: " + (codigo == 0 ? "OK" : "ERROR"));
            if (respuesta != null) System.out.println("PEDIDO: " + respuesta);
            if (error != null) System.out.println("DETALLE: " + error);
            System.out.println("CODIGO: " + codigo);
        }
    }
}
