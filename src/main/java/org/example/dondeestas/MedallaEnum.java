package org.example.dondeestas;

public enum  MedallaEnum {
    RESCATISTA1("Rescatista nivel 1"),
    RESCATISTA2("Rescatista nivel 2"),
    RESCATISTA3("Rescatista nivel 3"),
    HEROE_BARRIO("Héroe del barrio"),
    ANGEL_GUARDIAN("Ángel guardián"),
    NUEVO_TUTOR("Nuevo tutor");

    private final String descripcion;

    // Constructor privado
    MedallaEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
