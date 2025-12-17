package dondeestas.dto;

import jakarta.validation.constraints.NotBlank;

public class MascotaCrearDTO {

    @NotBlank
    private String nombre;

    private String tamano;
    private String color;
    private String fechaPerdida; // String o LocalDate según tu modelo
    private String ubicacion;
    private String estado;

    private String foto; // base64 opcional

    private Long usuarioId; // ahora viene en el body

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getFechaPerdida() { return fechaPerdida; }
    public void setFechaPerdida(String fechaPerdida) { this.fechaPerdida = fechaPerdida; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    @Override
    public String toString() {
        return "MascotaCrearDTO{" +
                "nombre='" + nombre + '\'' +
                ", tamano='" + tamano + '\'' +
                ", color='" + color + '\'' +
                ", fechaPerdida='" + fechaPerdida + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", estado='" + estado + '\'' +
                ", usuarioId=" + usuarioId +
                ", foto=" + (foto != null ? "[Base64... longitud: " + foto.length() + " chars]" : "null") +
                '}';
    }

}
