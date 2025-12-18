package dondeestas.auxClass;

public enum TipoAnimalEnum {

    PERRO("Perro"),
    GATO("Gato"),
    OTRO("Otro");

    private final String descripcion;

    TipoAnimalEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
