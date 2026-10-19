package org.svillaponce.psp.actividad06;

public class Actividad6TareaLenta {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) { System.err.println("Uso: <nombre> <segundos>"); System.exit(1); }
        long segundos;
        try {
            segundos = Long.parseLong(args[1]);
            if (segundos < 0 || segundos > Long.MAX_VALUE / 1000) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.err.println("Duración no válida"); System.exit(2); return;
        }
        System.out.println("Iniciando " + args[0]);
        Thread.sleep(segundos * 1000);
        System.out.println(args[0] + " completada");
    }
}
