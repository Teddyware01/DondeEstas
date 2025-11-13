package dondeestas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_medalla")
public class UsuarioMedalla {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "medalla_id", nullable = false)
    private Medalla medalla;

    @Column(name = "fecha_obtencion")
    private LocalDate fechaObtencion;

    // getters y setters
    public UsuarioMedalla() {}
    public UsuarioMedalla(Usuario usuario, Medalla medalla) {
        this.usuario = usuario;
        this.medalla = medalla;
    }

}