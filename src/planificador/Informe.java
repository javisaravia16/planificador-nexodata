package planificador;

import java.util.List;
import java.util.Locale;

/** Imprime por consola un Resultado: Gantt, tabla de métricas, cambios de contexto y traza. */
public class Informe {

    private static final Locale ES = Locale.forLanguageTag("es-ES");   // decimales con coma

    public static void imprimir(Resultado r, boolean conTraza) {
        System.out.println();
        System.out.println("=== " + r.nombreAlgoritmo() + " ===");
        imprimirGantt(r.gantt());
        System.out.println();
        imprimirTabla(r.procesos());
        System.out.println("Cambios de contexto: " + r.cambiosContexto());

        if (conTraza) {
            System.out.println();
            System.out.println("Traza de estados:");
            for (Transicion tr : r.traza()) {
                System.out.println(tr);
            }
        }
    }

    private static void imprimirGantt(List<String> gantt) {
        int ancho = 3;
        for (String s : gantt) {
            ancho = Math.max(ancho, s.length() + 1);
        }
        StringBuilder filaT = new StringBuilder(String.format("%-5s", "t"));
        StringBuilder filaCpu = new StringBuilder(String.format("%-5s", "CPU"));
        for (int t = 0; t < gantt.size(); t++) {
            filaT.append(String.format("%-" + ancho + "s", t));
            filaCpu.append(String.format("%-" + ancho + "s", gantt.get(t)));
        }
        filaT.append(gantt.size());   // instante final
        System.out.println(filaT);
        System.out.println(filaCpu);
    }

    private static void imprimirTabla(List<Proceso> procesos) {
        String formato = "%-10s %8s %7s %5s %8s %7s %10s%n";
        System.out.printf(formato, "Proceso", "Llegada", "Ráfaga", "Fin", "Retorno", "Espera", "Respuesta");

        double sumaRetorno = 0, sumaEspera = 0, sumaRespuesta = 0;
        for (Proceso p : procesos) {
            System.out.printf(formato, p.getNombre(), p.getLlegada(), p.getRafaga(), p.getFin(),
                    p.getRetorno(), p.getEspera(), p.getRespuesta());
            sumaRetorno += p.getRetorno();
            sumaEspera += p.getEspera();
            sumaRespuesta += p.getRespuesta();
        }
        int n = procesos.size();
        System.out.println(String.format(ES, "Medias: retorno %.2f | espera %.2f | respuesta %.2f",
                sumaRetorno / n, sumaEspera / n, sumaRespuesta / n));
    }
}
