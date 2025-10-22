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
    public UsuarioMedalla() {}
    public UsuarioMedalla(Usuario usuario, Medalla medalla) {
        this.usuario = usuario;
        this.medalla = medalla;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Medalla getMedalla() {
        return medalla;
    }

    public Long getId() {
        return id;
    }

    public void setMedalla(Medalla medalla) {
        medalla = medalla;
    }
}