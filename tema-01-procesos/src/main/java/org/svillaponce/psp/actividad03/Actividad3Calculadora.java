package org.svillaponce.psp.actividad03;

public class Actividad3Calculadora {

    static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("ERROR: Se necesitan dos argumentos.");
            System.exit(1);
        }

        String operacion = args[0];
        int numero = Integer.parseInt(args[1]);

        int resultado;

        switch (operacion) {
            case "CUADRADO" -> resultado = numero * numero;
            case "CUBO" -> resultado = numero * numero * numero;
            case "DOBLE" -> resultado = numero * 2;

            default -> {
                System.err.println("ERROR: Operación no válida.");
                System.exit(2);
                return;
            }
        }

        System.out.println("RESULTADO=" + resultado);
    }
}
