package org.svillaponce.psp.actividad09;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Actividad9ValidadorUsuarios {
    private static final Set<String> DOMINIOS = Set.of("centro.es", "educacion.es", "alumno.es");
    public static void main(String[] args) throws IOException {
        int global = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null && !linea.equals("FIN")) {
                int codigo = validar(linea);
                System.out.flush(); // El padre espera una respuesta antes de enviar el siguiente usuario.
                if (global == 0 && codigo != 0) global = codigo;
            }
        }
        // Para varios usuarios se conserva el código del primer error; cada respuesta describe su caso.
        System.exit(global);
    }
    private static int error(int codigo, String tipo, String mensaje) {
        System.out.println("ERROR|" + tipo + "|" + mensaje);
        return codigo;
    }
    private static int validar(String linea) {
        String[] c = linea.split(";", -1);
        if (c.length != 3) return error(1, "DATOS", "Se requieren nombre;email;edad");
        for (int i = 0; i < c.length; i++) c[i] = c[i].trim();
        if (c[0].isEmpty() || c[0].contains("|")) return error(2, "NOMBRE", "Nombre vacío o contiene separador del protocolo");
        if (!c[1].matches("[A-Za-z0-9.!#$%&'*+/=?^_`{}~-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+"))
            return error(3, "EMAIL", "Formato de correo incorrecto");
        String dominio = c[1].substring(c[1].indexOf('@') + 1).toLowerCase(Locale.ROOT);
        if (!DOMINIOS.contains(dominio)) return error(4, "DOMINIO", dominio + " no está permitido");
        int edad;
        try { edad = Integer.parseInt(c[2]); }
        catch (NumberFormatException e) { return error(5, "EDAD", "La edad no es un número entero"); }
        if (edad < 16 || edad > 100) return error(5, "EDAD", "La edad debe estar entre 16 y 100");
        System.out.println("OK|" + String.join("|", c));
        return 0;
    }
}
