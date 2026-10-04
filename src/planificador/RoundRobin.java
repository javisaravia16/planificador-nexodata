package planificador;

import java.util.List;

public class RoundRobin extends Planificador {

    private final int quantum;

    public RoundRobin(int quantum) {
        if (quantum <= 0) {
            throw new IllegalArgumentException("El quantum debe ser mayor que 0.");
        }
        this.quantum = quantum;
    }

    @Override
    public String getNombre() {
        return "Round Robin (q=" + quantum + ")";
    }

    @Override
    protected Proceso elegir(List<Proceso> listos) {
        return listos.remove(0);   // cola FIFO
    }

    @Override
    protected boolean debeDesalojar(int usadoEnTurno) {
        return usadoEnTurno >= quantum;
    }
}
