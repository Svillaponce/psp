package org.svillaponce.psp.ejemplo_clase_completo;

public class AlmacenPapel {
    private int hojas;

    AlmacenPapel(int hojasIniciales) {
        this.hojas = hojasIniciales;
    }

    public synchronized boolean reservar(int cantidad) {
        if (hojas < cantidad) {
            return false;
        }

        hojas -= cantidad;
        return true;
    }

//    public boolean reservar(int cantidad) {
//        int disponibles = hojas;
//
//        if (disponibles < cantidad) {
//            return false;
//        }
//
//        try {
//            Thread.sleep(150);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return false;
//        }
//
//        hojas = disponibles - cantidad;
//        return true;
//    }

    public int getHojas() {
        return hojas;
    }
}
