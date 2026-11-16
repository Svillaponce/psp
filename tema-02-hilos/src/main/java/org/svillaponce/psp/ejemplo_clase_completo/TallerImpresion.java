package org.svillaponce.psp.ejemplo_clase_completo;

public class TallerImpresion {
    void main() throws InterruptedException {
        AlmacenPapel almacen = new AlmacenPapel(8);
        TrabajoImpresion trabajoA = new TrabajoImpresion(almacen, 4);
        TrabajoImpresion trabajoB = new TrabajoImpresion(almacen, 4);

        Thread impresoraA = new Thread(trabajoA, "Impresora A");
        Thread impresoraB = new Thread(trabajoB, "Impresora B");

        impresoraA.start();
        impresoraB.start();

        impresoraA.join();
        impresoraB.join();

        int aceptados = (trabajoA.fueAceptado() ? 1 : 0)
                + (trabajoB.fueAceptado() ? 1 : 0);

        System.out.println("Trabajos aceptados: " + aceptados);
        System.out.println("Hojas disponibles: " + almacen.getHojas());
    }
}
