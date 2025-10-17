package org.example.dondeestas;

public enum EstadoEnum {
    PERDIDO_PROPIO("Mascota perdida, publicada por su dueño"),
    PERDIDO_AJENO("Mascota perdida, publicada por quien la encontro"),
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
