package planificador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LectorProcesos {

    public static List<Proceso> leer(Path ruta) throws IOException {
        List<String> lineas = Files.readAllLines(ruta);
        List<Proceso> procesos = new ArrayList<>();

        int numLinea = 0;
        for (String linea : lineas) {
            numLinea++;
            String limpia = linea.trim();

            if (limpia.isEmpty() || limpia.startsWith("#")) {
                continue; // línea vacía o comentario: se ignora
            }

            procesos.add(parsearLinea(limpia, numLinea, procesos.size()));
        }

        if (procesos.isEmpty()) {
            throw new IllegalArgumentException("El fichero " + ruta + " no contiene ningún proceso.");
        }
        return procesos;
    }

    private static Proceso parsearLinea(String linea, int numLinea, int orden) {
        String[] partes = linea.split(";");

        if (partes.length != 3) {
            throw new IllegalArgumentException("Línea " + numLinea
                    + ": se esperaban 3 campos (nombre;llegada;ráfaga) y hay "
                    + partes.length + " -> \"" + linea + "\"");
        }

        String nombre = partes[0].trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("Línea " + numLinea + ": el nombre está vacío.");
        }

        int llegada = aEntero(partes[1], "llegada", numLinea);
        int rafaga = aEntero(partes[2], "ráfaga", numLinea);

        if (llegada < 0) {
            throw new IllegalArgumentException("Línea " + numLinea
                    + ": la llegada no puede ser negativa (" + llegada + ").");
        }
        if (rafaga <= 0) {
            throw new IllegalArgumentException("Línea " + numLinea
                    + ": la ráfaga debe ser mayor que 0 (" + rafaga + ").");
        }

        return new Proceso(nombre, llegada, rafaga, orden);
    }

    private static int aEntero(String texto, String campo, int numLinea) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Línea " + numLinea
                    + ": la " + campo + " no es un número entero -> \"" + texto.trim() + "\"");
        }
    }
}
