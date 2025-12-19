package dondeestas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class MascotaCrearDTO {

    // Getters y setters
    @NotBlank
    private String nombre;

    private String tamano;
    private String color;
    private String fechaPerdida; // String o LocalDate según tu modelo
    private String ubicacion;
    private String estado;
    private String descripcionExtra;
    private String tipoAnimal;

    private String foto; // base64 opcional

    private Long usuarioId; // ahora viene en el body


    @Setter
    private List<String> imagenes;

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
