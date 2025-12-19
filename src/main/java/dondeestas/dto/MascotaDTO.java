package dondeestas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MascotaDTO {

    private Long id;
    private String nombre;
    private String tamano;
    private String color;
    private String estado;
    private String tipoAnimal;
    private String descripcionExtra;
    private LocalDate fecha;

    private String provincia;
    private String departamento;
    private String municipio;
    private String telefono;

    private List<String> imagenesBase64;

    public MascotaDTO() {
    }

    public MascotaDTO(Long id, String nombre, String tamano, String color,
                      String estado,String tipoAnimal, LocalDate fecha,
                      String provincia, String departamento, String municipio,String descripcionExtra,String telefono,
                      List<String> imagenesBase64) {
        this.id = id;
        this.nombre = nombre;
        this.tamano = tamano;
        this.color = color;
        this.estado = estado;
        this.tipoAnimal = tipoAnimal;
        this.fecha = fecha;
        this.descripcionExtra=descripcionExtra;
        this.provincia = provincia;
        this.departamento = departamento;
        this.municipio = municipio;
        this.imagenesBase64 = imagenesBase64;
        this.telefono = telefono;
    }
}
