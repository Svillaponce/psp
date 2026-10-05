package org.svillaponce.psp.actividad04;

public class Actividad4ProcesamientoException extends RuntimeException {
    private final Actividad4TipoError tipo;

    public Actividad4ProcesamientoException(Actividad4TipoError tipo, String message) {
        super(message);
        this.tipo = tipo;
    }

    public Actividad4TipoError getTipo() {
        return tipo;
    }
}
