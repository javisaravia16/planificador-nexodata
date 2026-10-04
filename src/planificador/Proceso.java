package planificador;

public class Proceso {

    // Datos del fichero: no cambian nunca
    private final String nombre;
    private final int llegada;
    private final int rafaga;
    private final int orden;        // posición en el fichero (0, 1, 2...) para desempates

    // Datos que cambian durante la simulación
    private int restante;
    private EstadoProceso estado;
    private int primeraEjecucion;   // -1 = todavía no ha usado la CPU
    private int fin;                // -1 = todavía no ha terminado

    public Proceso(String nombre, int llegada, int rafaga, int orden) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafaga = rafaga;
        this.orden = orden;
        this.restante = rafaga;
        this.estado = EstadoProceso.NUEVO;
        this.primeraEjecucion = -1;
        this.fin = -1;
    }

    /** Devuelve un proceso nuevo con los mismos datos del fichero y la simulación a cero. */
    public Proceso copia() {
        return new Proceso(nombre, llegada, rafaga, orden);
    }

    /** Ejecuta una unidad de CPU en el instante t. */
    public void ejecutarUnidad(int t) {
        if (primeraEjecucion == -1) {
            primeraEjecucion = t;
        }
        restante--;
    }

    public boolean haTerminado() {
        return restante == 0;
    }

    // ---- Métricas (solo tienen sentido cuando el proceso ha terminado) ----

    public int getRetorno() {
        return fin - llegada;
    }

    public int getEspera() {
        return getRetorno() - rafaga;
    }

    public int getRespuesta() {
        return primeraEjecucion - llegada;
    }

    // ---- Getters y setters ----

    public String getNombre() { return nombre; }
    public int getLlegada() { return llegada; }
    public int getRafaga() { return rafaga; }
    public int getOrden() { return orden; }
    public int getRestante() { return restante; }
    public EstadoProceso getEstado() { return estado; }
    public int getFin() { return fin; }

    public void setEstado(EstadoProceso estado) { this.estado = estado; }
    public void setFin(int fin) { this.fin = fin; }

    @Override
    public String toString() {
        return nombre + " (llegada=" + llegada + ", ráfaga=" + rafaga + ")";
    }
}
