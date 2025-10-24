package dondeestas;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "avistamientos")
public class Avistamiento {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

/*    public Avistamiento(Mascota mascota, Ubicacion ubiAvistamiento, String fotoPath, LocalDate now, String s, Usuario usuario) {
        this.mascota = mascota;
        this.ubicacion = ubiAvistamiento;
        this.foto = fotoPath;
        this.fecha = LocalDateTime.now();
        this.comentario = s;
        this.usuario = usuario;
    }*/

    public Usuario getUsuarioAvistador() {
        return usuario;
    }

    public Avistamiento() {}
    public Avistamiento(String foto, LocalDateTime fecha, String comentario, Mascota mascota, Usuario usuario, Ubicacion ubicacion) {
        this.foto = foto;
        this.fecha = fecha;
        this.comentario = comentario;
        this.mascota = mascota;
        this.usuario = usuario;
        this.ubicacion = ubicacion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public long getId() {
        return id;
    }

    public Mascota getMascota() {
        return mascota;
    }
   public Usuario getUsuario() {
        return usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFoto() {
        return foto;

    }
}
