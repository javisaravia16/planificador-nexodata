package planificador;

import java.util.ArrayList;
import java.util.List;

/**
 * Bucle de simulación común a todos los algoritmos.
 * Las subclases solo deciden dos cosas:
 *   - elegir(): a quién se da la CPU cuando está libre.
 *   - debeDesalojar(): si hay que quitarle la CPU al proceso actual.
 */
public abstract class Planificador {

    public abstract String getNombre();

    /** Saca de la cola de listos el proceso que va a entrar en la CPU. La cola nunca está vacía. */
    protected abstract Proceso elegir(List<Proceso> listos);

    /** ¿Hay que quitarle la CPU al proceso actual, que ya lleva 'usadoEnTurno' unidades seguidas? */
    protected abstract boolean debeDesalojar(int usadoEnTurno);

    public Resultado simular(List<Proceso> originales) {
        // Trabajamos con copias: los originales quedan intactos para el siguiente algoritmo
        List<Proceso> procesos = new ArrayList<>();
        for (Proceso p : originales) {
            procesos.add(p.copia());
        }

        List<Proceso> listos = new ArrayList<>();   // cola de listos (el orden importa)
        List<String> gantt = new ArrayList<>();
        List<Transicion> traza = new ArrayList<>();

        Proceso actual = null;        // proceso en la CPU (null = CPU libre)
        Proceso ultimo = null;        // último proceso que usó la CPU
        int usadoEnTurno = 0;
        int cambiosContexto = 0;
        int terminados = 0;
        int t = 0;

        while (terminados < procesos.size()) {

            // 1) Llegadas en t (en orden de fichero: regla 2)
            for (Proceso p : procesos) {
                if (p.getLlegada() == t) {
                    cambiarEstado(p, EstadoProceso.LISTO, t, "llega al sistema", traza);
                    listos.add(p);
                }
            }

            // 2) ¿El proceso en CPU ha terminado o hay que desalojarlo?
            if (actual != null) {
                if (actual.haTerminado()) {
                    actual.setFin(t);
                    cambiarEstado(actual, EstadoProceso.TERMINADO, t, "completa su ráfaga", traza);
                    terminados++;
                    actual = null;
                } else if (debeDesalojar(usadoEnTurno)) {
                    if (listos.isEmpty()) {
                        usadoEnTurno = 0;   // regla 4: nadie espera, sigue con quantum nuevo
                    } else {
                        cambiarEstado(actual, EstadoProceso.LISTO, t, "agota el quantum", traza);
                        listos.add(actual); // va detrás de los que acaban de llegar (regla 3)
                        actual = null;
                    }
                }
            }

            if (terminados == procesos.size()) {
                break;
            }

            // 3) Si la CPU está libre, el algoritmo elige
            if (actual == null && !listos.isEmpty()) {
                actual = elegir(listos);
                cambiarEstado(actual, EstadoProceso.EJECUCION, t, "el planificador lo elige", traza);
                usadoEnTurno = 0;
                if (ultimo != null && ultimo != actual) {
                    cambiosContexto++;
                }
                ultimo = actual;
            }

            // 4) Ejecutar una unidad y avanzar
            if (actual != null) {
                actual.ejecutarUnidad(t);
                usadoEnTurno++;
                gantt.add(actual.getNombre());
            } else {
                gantt.add("-");   // CPU ociosa
            }
            t++;
        }

        return new Resultado(getNombre(), procesos, gantt, cambiosContexto, traza);
    }

    private void cambiarEstado(Proceso p, EstadoProceso nuevo, int t, String motivo,
                               List<Transicion> traza) {
        traza.add(new Transicion(t, p.getNombre(), p.getEstado(), nuevo, motivo));
        p.setEstado(nuevo);
    }
}
