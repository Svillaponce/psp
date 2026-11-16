package org.svillaponce.psp.problema_integrador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProblemaIntegradorCentralPedidos {
    private static void ejecutar(int teclados) throws InterruptedException {
        System.out.println("=== Stock inicial TEC: " + teclados + ", RAT: 5 ===");
        Inventario inventario = new Inventario(teclados);
        List<Pedido> lote = List.of(new Pedido("PED-01", "TEC", 4),
                new Pedido("PED-02", "TEC", 4), new Pedido("PED-03", "RAT", 2),
                new Pedido("PED-04", "RAT", 3), new Pedido("PED-05", "RAT", 0),
                new Pedido("PED-06", "XXX", 1));
        ExecutorService ejecutor = Executors.newFixedThreadPool(3);
        try {
            List<Future<TareaPedido.Resultado>> futuros = new ArrayList<>();
            for (Pedido pedido : lote) {
                futuros.add(ejecutor.submit(new TareaPedido(inventario, pedido)));
            }
            int completados = 0, rechazados = 0, errores = 0;
            for (int i = 0; i < futuros.size(); i++) {
                try {
                    TareaPedido.Resultado resultado = futuros.get(i).get();
                    System.out.println(resultado.id() + " | "
                            + (resultado.completado() ? "COMPLETADO" : "RECHAZADO"));
                    if (resultado.completado()) completados++;
                    else rechazados++;
                } catch (ExecutionException e) {
                    errores++;
                    System.out.println(lote.get(i).id() + " | ERROR | " + e.getCause());
                }
            }
            System.out.printf("Completados: %d, rechazados: %d, errores: %d%n",
                    completados, rechazados, errores);
            Map<String, Integer> stock = inventario.consultarStock();
            System.out.printf("Stock final: TEC=%d, RAT=%d%n", stock.get("TEC"), stock.get("RAT"));
        } finally {
            ejecutor.shutdown();
            ejecutor.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ejecutar(6);
        ejecutar(8);
    }
}
