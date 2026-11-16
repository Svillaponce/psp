package org.svillaponce.psp.ejemplo_clase_completo;

final class Buzon {

    private Solicitud pendiente;

    public synchronized void put(Solicitud solicitud)
            throws InterruptedException {
        while (pendiente != null) {
            wait();
        }

        pendiente = solicitud;
        notifyAll();
    }

    public synchronized Solicitud take()
            throws InterruptedException {
        while (pendiente == null) {
            wait();
        }

        Solicitud solicitud = pendiente;
        pendiente = null;
        notifyAll();

        return solicitud;
    }
}
