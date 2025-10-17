package dondeestas;

public enum TipoPuntosEnum {
    HALLAZGO("Hallazgo"),
    REPORTE_AVISTAMIENTO("Reporte de avistamiento"),
    ADOPCION("Adopción");

    private final String descripcion;

    TipoPuntosEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
