package dondeestas;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_medalla")
public class UsuarioMedalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "medalla_id", nullable = false)
    private Medalla medalla;

    @Column
    private LocalDate fechaObtencion;

    // getters y setters
}