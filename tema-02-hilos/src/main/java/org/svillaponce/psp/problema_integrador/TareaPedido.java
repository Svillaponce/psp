package org.svillaponce.psp.problema_integrador;

import java.util.concurrent.Callable;

public class TareaPedido implements Callable<TareaPedido.Resultado> {
    public record Resultado(String id, boolean completado) { }

    private final Inventario inventario;
    private final Pedido pedido;

    public TareaPedido(Inventario inventario, Pedido pedido) {
        this.inventario = inventario;
        this.pedido = pedido;
    }

    @Override
    public Resultado call() throws InterruptedException {
        if (!inventario.reservar(pedido.producto(), pedido.unidades())) {
            return new Resultado(pedido.id(), false);
        }
        boolean completado = false;
        try {
            // Preparar no retiene el monitor del inventario.
            Thread.sleep(40);
            if (Thread.currentThread().isInterrupted()) throw new InterruptedException("Preparación cancelada");
            Resultado resultado = new Resultado(pedido.id(), true);
            completado = true; // Punto de confirmación del pedido.
            return resultado;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        } finally {
            // Un único propietario y un único punto de devolución evitan duplicarla.
            if (!completado) inventario.devolver(pedido.producto(), pedido.unidades());
        }
    }
}
