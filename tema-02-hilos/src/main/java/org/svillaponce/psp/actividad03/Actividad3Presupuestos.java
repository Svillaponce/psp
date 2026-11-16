package org.svillaponce.psp.actividad03;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Actividad3Presupuestos {
    public enum Tipo {
        SENCILLA(8), COMPLEJA(14);
        private final int precio;
        Tipo(int precio) { this.precio = precio; }
    }

    public record Solicitud(String id, Tipo tipo, int cantidad) { }
    public record Resultado(String id, int importe, boolean aceptada) { }

    public static Callable<Resultado> presupuestar(Solicitud solicitud) {
        return () -> {
            if (solicitud.cantidad() <= 0) {
                throw new IllegalArgumentException("Cantidad no positiva: " + solicitud.cantidad());
            }
            int importe = Math.multiplyExact(solicitud.tipo().precio, solicitud.cantidad());
            return new Resultado(solicitud.id(), importe, importe <= 100);
        };
    }

    private static void ejecutar(int cantidadP02) throws InterruptedException {
        System.out.println("=== P02 con " + cantidadP02 + " unidades ===");
        List<Solicitud> lote = List.of(new Solicitud("P01", Tipo.SENCILLA, 3),
                new Solicitud("P02", Tipo.COMPLEJA, cantidadP02),
                new Solicitud("P03", Tipo.COMPLEJA, 0), new Solicitud("P04", Tipo.COMPLEJA, 5));
        ExecutorService ejecutor = Executors.newFixedThreadPool(2);
        try {
            List<Future<Resultado>> futuros = new ArrayList<>();
            for (Solicitud solicitud : lote) {
                futuros.add(ejecutor.submit(presupuestar(solicitud)));
            }
            ejecutor.shutdown(); // No admite más tareas; las enviadas siguen ejecutándose.
            int aceptadas = 0, rechazadas = 0, errores = 0, total = 0;
            for (int i = 0; i < futuros.size(); i++) {
                try {
                    Resultado resultado = futuros.get(i).get();
                    System.out.printf("%s | %d € | %s%n", resultado.id(), resultado.importe(),
                            resultado.aceptada() ? "ACEPTADA" : "RECHAZADA");
                    if (resultado.aceptada()) {
                        aceptadas++;
                        total += resultado.importe();
                    } else {
                        rechazadas++;
                    }
                } catch (ExecutionException e) {
                    errores++;
                    System.out.println(lote.get(i).id() + " | ERROR | " + e.getCause());
                }
            }
            System.out.printf("Aceptadas: %d, rechazadas: %d, errores: %d, total aceptado: %d €%n",
                    aceptadas, rechazadas, errores, total);
        } finally {
            // close espera la terminación; también cierra si get es interrumpido.
            ejecutor.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ejecutar(8);
        ejecutar(7);
    }
}
