package dondeestas;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_puntaje")
public class UsuarioPuntaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "puntaje_id", nullable = false)
    private Puntaje puntaje;

    @Column
    private LocalDate fechaObtencion;

}