package planificador;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

/**
 * PSP · Tema 2 · Simulador de planificación para NexoData
 *
 * Punto de entrada. Este fichero YA ESTÁ HECHO: solo lee y comprueba los argumentos.
 * No cambies cómo se leen: el profesor ejecutará tu programa siempre así:
 *
 *   java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]
 *
 * Todo lo demás (modelo del proceso, lectura del CSV, algoritmos, métricas,
 * informe por consola...) lo diseñas y programas tú en este mismo paquete.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]");
            System.exit(1);
        }
        Path fichero = Path.of(args[0]);
        String algoritmo = args[1].toLowerCase();
        boolean traza = List.of(args).contains("--traza");
        int quantum = 2;
        if (args.length >= 3 && !args[2].startsWith("--")) {
            try {
                quantum = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("El quantum debe ser un número entero: " + args[2]);
                System.exit(1);
            }
        }
        if (!Files.exists(fichero)) {
            System.err.println("No encuentro el fichero " + fichero.toAbsolutePath()
                    + "\nComprueba el «Working directory» de la configuración de ejecución.");
            System.exit(1);
        }
        if (!List.of("fcfs", "sjf", "rr", "todos").contains(algoritmo)) {
            System.err.println("Algoritmo desconocido: " + algoritmo + " (usa fcfs, sjf, rr o todos)");
            System.exit(1);
        }


        try {
            List<Proceso> procesos = LectorProcesos.leer(fichero);

            List<Planificador> planificadores = new ArrayList<>();
            switch (algoritmo) {
                case "fcfs" -> planificadores.add(new FCFS());
                case "sjf" -> planificadores.add(new SJF());
                case "rr" -> planificadores.add(new RoundRobin(quantum));
                case "todos" -> {
                    planificadores.add(new FCFS());
                    planificadores.add(new SJF());
                    planificadores.add(new RoundRobin(quantum));
                }
            }

            for (Planificador p : planificadores) {
                Resultado r = p.simular(procesos);   // simular() trabaja con copias
                Informe.imprimir(r, traza);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer el fichero: " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}