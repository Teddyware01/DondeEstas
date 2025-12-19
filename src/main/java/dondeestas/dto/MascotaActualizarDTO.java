package dondeestas.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class MascotaActualizarDTO {

    private String nombre;          // Opcional, puede cambiar
    private String tamano;
    private String color;
    private String fechaPerdida;    // yyyy-MM-dd como string
    private String ubicacion;       // "lat,lng"
    private String estado;          // PERDIDO_PROPIO, RECUPERADO, etc.
    private String descripcionExtra;
    private String tipoAnimal;      // PERRO, GATO, etc.


    @Override
    public String toString() {
        return "MascotaActualizarDTO{" +
                "nombre='" + nombre + '\'' +
                ", tamano='" + tamano + '\'' +
                ", color='" + color + '\'' +
                ", fechaPerdida='" + fechaPerdida + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", estado='" + estado + '\'' +
                ", descripcionExtra='" + descripcionExtra + '\'' +
                ", tipoAnimal='" + tipoAnimal + '\'' +
                '}';
    }
}
