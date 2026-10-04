package planificador;

import java.util.List;

public class FCFS extends Planificador {

    @Override
    public String getNombre() {
        return "FCFS";
    }

    @Override
    protected Proceso elegir(List<Proceso> listos) {
        return listos.remove(0);   // el primero de la cola
    }

    @Override
    protected boolean debeDesalojar(int usadoEnTurno) {
        return false;              // sin desalojo
    }
}
