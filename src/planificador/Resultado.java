package planificador;

import java.util.List;

/** Todo lo que produce una simulación: lo que luego se imprime por consola. */
public record Resultado(String nombreAlgoritmo,
                        List<Proceso> procesos,
                        List<String> gantt,
                        int cambiosContexto,
                        List<Transicion> traza) {
}
