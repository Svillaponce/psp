package org.svillaponce.psp.actividad04;

public class Actividad4ProcesadorPedido {
    static void main(String[] args) {
        try {
            validarArgumentos(args);

            String producto = args[0];
            int cantidad = Integer.parseInt(args[1]);
            double precio = Double.parseDouble(args[2]);

            validarPedido(producto, cantidad, precio);

            double total = cantidad * precio;
            System.out.printf(
                    "OK|%s|%d|%.2f|%.2f%n",
                    producto,
                    cantidad,
                    precio,
                    total
            );

            System.exit(0);
        } catch (Actividad4ProcesamientoException e) {
            System.err.println("ERROR: " + e.getTipo() + " | " + e.getMessage());

            System.exit(codigoSalida(e.getTipo()));
        }


    }

    public static void validarArgumentos(String[] args) throws Actividad4ProcesamientoException {
        if (args.length != 3) {
            throw new Actividad4ProcesamientoException(Actividad4TipoError.ARGUMENTOS, "Se necesitan 3 argumentos (Cod(S), Can(int), P(d))");
        }

        try {
            int cantidad = Integer.parseInt(args[1]);
            if (cantidad <= 0) {
                throw new Actividad4ProcesamientoException(Actividad4TipoError.FORMATO, "La cantidad debe ser un numero entero mayor que 0");
            }
        } catch (NumberFormatException e) {
            throw new Actividad4ProcesamientoException(Actividad4TipoError.CANTIDAD, "La cantidad debe ser un numero entero mayor que 0");
        }

        try {
            double precio = Double.parseDouble(args[2]);
            if (precio <= 0) {
                throw new Actividad4ProcesamientoException(Actividad4TipoError.FORMATO, "El precio debe ser un numero decimal mayor que 0");
            }
        } catch (NumberFormatException e) {
            throw new Actividad4ProcesamientoException(Actividad4TipoError.PRECIO, "El precio debe ser un numero entero mayor que 0");
        }
    }

    private static void validarPedido(String producto, int cantidad, double precio)
            throws Actividad4ProcesamientoException {

        if (!producto.matches("P\\d{3}")) {
            throw new Actividad4ProcesamientoException(
                    Actividad4TipoError.PRODUCTO,
                    "El producto debe tener formato P seguido de tres cifras"
            );
        }
    }

    public static int codigoSalida(Actividad4TipoError tipo) {
        return switch (tipo) {
            case ARGUMENTOS -> 1;
            case PRODUCTO -> 2;
            case CANTIDAD -> 3;
            case PRECIO -> 4;
            case FORMATO -> 5;
            default -> -1;
        };
    }
}
