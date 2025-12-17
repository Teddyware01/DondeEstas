package dondeestas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mascota_imagenes")
public class MascotaImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Getter
    @Setter
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGTEXT")
    private String imagenBase64;

    public MascotaImagen() {
    }

    public MascotaImagen(Mascota mascota, String imagenBase64) {
        this.mascota = mascota;
        this.imagenBase64 = imagenBase64;
    }
}
