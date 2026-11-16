package org.svillaponce.psp.problema_integrador;

import java.util.HashMap;
import java.util.Map;

public class Inventario {
    private final Map<String, Integer> stock = new HashMap<>();

    public Inventario(int teclados) {
        if (teclados < 0) throw new IllegalArgumentException("Stock inicial negativo");
        stock.put("TEC", teclados);
        stock.put("RAT", 5);
    }

    public synchronized boolean reservar(String producto, int unidades) {
        if (unidades <= 0) throw new IllegalArgumentException("Cantidad no positiva: " + unidades);
        Integer disponibles = stock.get(producto);
        if (disponibles == null) throw new IllegalArgumentException("Producto desconocido: " + producto);
        if (disponibles < unidades) return false;
        stock.put(producto, disponibles - unidades);
        return true;
    }

    // Solo la tarea propietaria de una reserva exitosa debe compensarla.
    synchronized void devolver(String producto, int unidades) {
        stock.put(producto, stock.get(producto) + unidades);
    }

    public synchronized Map<String, Integer> consultarStock() {
        return Map.copyOf(stock);
    }
}
