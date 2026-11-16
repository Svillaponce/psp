package org.svillaponce.psp.ejemplo_clase_completo;

public class TrabajoImpresion implements Runnable {

    private final AlmacenPapel almacen;
    private final int cantidad;
    private boolean aceptado;

    TrabajoImpresion(AlmacenPapel almacen, int cantidad) {
        this.almacen = almacen;
        this.cantidad = cantidad;
    }

    @Override
    public void run() {
        aceptado = almacen.reservar(cantidad);
        String estado = aceptado ? "ACEPTADO" : "RECHAZADO";

        System.out.printf("%s solicita %d hojas → %s%n",
                Thread.currentThread().getName(), cantidad, estado);
    }

    public boolean fueAceptado() {
        return aceptado;
    }
}
