package dondeestas.auxClass;

public enum EstadoEnum {

    PERDIDO_PROPIO("Perdido propio"),
    PERDIDO_AJENO("Perdido ajeno"),
    RECUPERADO("Recuperado"),
    ADOPTADO("Adoptado");

    private final String descripcion;

    EstadoEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
