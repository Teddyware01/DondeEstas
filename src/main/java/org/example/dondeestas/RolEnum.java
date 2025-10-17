package org.example.dondeestas;

public enum RolEnum {
    ESTANDAR("Estandar"),
    ADMIN("Administrador");

    private final String descripcion;

    RolEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
