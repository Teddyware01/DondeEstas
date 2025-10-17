package dondeestas;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avistamientos")
public class Avistamiento {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "usuario_avistador_id")
    private Usuario usuarioAvistador;

    @OneToOne
    private Ubicacion ubicacion;

    @Column
    private String foto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 500)
    private String comentario;

    public Mascota getMascota() {
        return mascota;
    }

    public Usuario getUsuarioAvistador() {
        return usuarioAvistador;
    }

    public Ubicacion getUbicacion(){
        return ubicacion;
    }

    public Avistamiento() {}
}
