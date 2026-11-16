package org.svillaponce.psp.actividad02;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Actividad2Laboratorio {
    private static final Muestra FIN = new Muestra("FIN", "Sin prioridad");

    private static Thread tecnico(BlockingQueue<Muestra> mesa, String nombre) {
        return new Thread(() -> {
            try {
                while (true) {
                    Muestra muestra = mesa.take();
                    if (muestra == FIN) {
                        System.out.println(nombre + " | salida por señal de fin");
                        return;
                    }
                    // La muestra ya no ocupa espacio en la mesa mientras se clasifica.
                    Thread.sleep(30);
                    System.out.printf("%s | %s | %s | procesada%n",
                            Thread.currentThread().getName(), muestra.id(), muestra.prioridad());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(nombre + " | salida por interrupción");
            }
        }, nombre);
    }

    private static void loteCompleto() throws InterruptedException {
        System.out.println("=== Lote completo ===");
        BlockingQueue<Muestra> mesa = new ArrayBlockingQueue<>(2);
        Thread tecnico1 = tecnico(mesa, "Técnico 1");
        Thread tecnico2 = tecnico(mesa, "Técnico 2");
        Thread recepcion = new Thread(() -> {
            try {
                for (Muestra muestra : List.of(new Muestra("M01", "Normal"),
                        new Muestra("M02", "Urgente"), new Muestra("M03", "Normal"),
                        new Muestra("M04", "Urgente"), new Muestra("M05", "Normal"))) {
                    mesa.put(muestra);
                }
                mesa.put(FIN);
                mesa.put(FIN);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                tecnico1.interrupt();
                tecnico2.interrupt();
                System.out.println("Recepción | salida por interrupción");
            }
        }, "Recepción");
        tecnico1.start();
        tecnico2.start();
        recepcion.start();
        recepcion.join();
        tecnico1.join();
        tecnico2.join();
    }

    private static void cancelarConMesaVacia() throws InterruptedException {
        System.out.println("=== Cancelación con cola vacía ===");
        BlockingQueue<Muestra> mesa = new ArrayBlockingQueue<>(2);
        Thread tecnico1 = tecnico(mesa, "Técnico 1");
        Thread tecnico2 = tecnico(mesa, "Técnico 2");
        tecnico1.start();
        tecnico2.start();
        // interrupt funciona incluso si llega antes de que el hilo entre en take().
        tecnico1.interrupt();
        tecnico2.interrupt();
        tecnico1.join();
        tecnico2.join();
    }

    public static void main(String[] args) throws InterruptedException {
        loteCompleto();
        cancelarConMesaVacia();
    }
}
