package org.svillaponce.psp.ejemplo_clase_completo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TallerConCola {

    private static final Solicitud FIN = new Solicitud("FIN", 0);

    void main() throws InterruptedException {
        AlmacenPapel almacen = new AlmacenPapel(6);
        BlockingQueue<Solicitud> cola = new ArrayBlockingQueue<>(1);

        Runnable consumir = () -> {
            String impresora = Thread.currentThread().getName();

            try {
                while (true) {
                    Solicitud solicitud = cola.take();

                    if (solicitud == FIN) {
                        System.out.println(impresora + ": fin de los trabajos");
                        return;
                    }

                    boolean aceptada = almacen.reservar(solicitud.hojas());

                    System.out.printf("%s · Trabajo %s · %s%n",
                            impresora,
                            solicitud.id(),
                            aceptada ? "ACEPTADO" : "RECHAZADO");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(impresora + ": cancelada");
            }
        };

        Thread impresoraA = new Thread(consumir, "Impresora A");
        Thread impresoraB = new Thread(consumir, "Impresora B");

        impresoraA.start();
        impresoraB.start();

        cola.put(new Solicitud("A", 4));
        cola.put(new Solicitud("B", 4));

        cola.put(FIN);
        cola.put(FIN);

        impresoraA.join();
        impresoraB.join();

        System.out.println("Hojas disponibles: " + almacen.getHojas());
    }
}
