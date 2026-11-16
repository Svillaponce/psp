package org.svillaponce.psp.actividad01;

import java.util.List;

public class Actividad1Accesos {
    private static Runnable puerta(RegistroEntradas registro, List<String> lecturas) {
        return () -> {
            for (String codigo : lecturas) {
                RegistroEntradas.Resultado resultado = registro.registrar(codigo);
                System.out.printf("%s | %s | %s%n",
                        Thread.currentThread().getName(), codigo, resultado);
            }
        };
    }

    private static void ejecutar(boolean extra) throws InterruptedException {
        System.out.println(extra ? "=== E02 adicional ===" : "=== Datos de partida ===");
        RegistroEntradas registro = new RegistroEntradas();
        List<String> norte = extra ? List.of("E01", "E03", "E02", "X99", "E02")
                : List.of("E01", "E03", "E02", "X99");
        Thread puertaNorte = new Thread(puerta(registro, norte), "Puerta Norte");
        Thread puertaSur = new Thread(puerta(registro,
                List.of("E03", "E04", "E01", "E05")), "Puerta Sur");
        puertaNorte.start();
        puertaSur.start();
        puertaNorte.join();
        puertaSur.join();
        System.out.println(registro.resumen());
    }

    public static void main(String[] args) throws InterruptedException {
        ejecutar(false);
        ejecutar(true);
    }
}
