package dondeestas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "avistamientos")
public class Avistamiento {


    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Getter
    @JsonIgnore
    @Setter
    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Setter
    @Getter
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = true)
    private Ubicacion ubicacion;

    @Column
    @Nullable
    private String foto;


    @Setter
    @Getter
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Setter
    @Getter
    @Column(length = 500)
    private String comentario;

    public Avistamiento() {}
    public Avistamiento(String foto, LocalDateTime fecha, String comentario, Mascota mascota, Usuario usuario, Ubicacion ubicacion) {
        this.foto = foto;
        this.fecha = fecha;
        this.comentario = comentario;
        this.mascota = mascota;
        this.usuario = usuario;
        this.ubicacion = ubicacion;
    }





}
