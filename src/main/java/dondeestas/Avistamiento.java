package dondeestas;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avistamientos")
public class Avistamiento {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;

    @Column
    private String foto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 500)
    private String comentario;

    public Usuario getUsuarioAvistador() {
        return usuario;
    }

    public Avistamiento() {}
}
