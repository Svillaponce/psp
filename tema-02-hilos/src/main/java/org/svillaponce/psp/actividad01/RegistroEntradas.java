package org.svillaponce.psp.actividad01;

import java.util.HashSet;
import java.util.Set;

/** Un único monitor protege el registro y sus contadores. */
public class RegistroEntradas {
    public enum Resultado { ADMITIDA, DUPLICADA, INVALIDA }

    private final Set<String> validas = Set.of("E01", "E02", "E03", "E04", "E05");
    private final Set<String> usadas = new HashSet<>();
    private int admitidas;
    private int duplicadas;
    private int invalidas;

    public synchronized Resultado registrar(String codigo) {
        if (!validas.contains(codigo)) {
            invalidas++;
            return Resultado.INVALIDA;
        }
        if (!usadas.add(codigo)) {
            duplicadas++;
            return Resultado.DUPLICADA;
        }
        admitidas++;
        return Resultado.ADMITIDA;
    }

    public synchronized String resumen() {
        return "Admitidas: " + admitidas + ", duplicadas: " + duplicadas
                + ", inválidas: " + invalidas;
    }
}
