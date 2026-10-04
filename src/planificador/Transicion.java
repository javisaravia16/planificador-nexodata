package planificador;

/** Una línea de la traza: en el instante t, el proceso pasó de un estado a otro por un motivo. */
public record Transicion(int t, String proceso, EstadoProceso origen,
                         EstadoProceso destino, String motivo) {

    @Override
    public String toString() {
        return "t=" + t + "  " + proceso + "  " + origen + " -> " + destino + "  (" + motivo + ")";
    }
}
