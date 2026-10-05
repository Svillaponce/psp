package org.svillaponce.psp.actividad06;

public class Actividad6TareaLenta {

    static void main(String[] args)
            throws InterruptedException {

        if (args.length != 1) {
            System.err.println(
                    "ERROR: Debe indicarse la duración de la tarea."
            );
            System.exit(1);
        }

        int segundos;

        try {
            segundos = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println(
                    "ERROR: La duración debe ser un número entero."
            );
            System.exit(2);
            return;
        }

        System.out.println(
                "Tarea iniciada. Duración: "
                        + segundos + " segundos."
        );

        Thread.sleep(segundos * 1000L);

        System.out.println("Tarea terminada correctamente.");
    }
}