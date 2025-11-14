package dondeestas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    @Getter
    @Setter    @JsonIgnore
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Avistamiento> avistamientos;

    @Getter
    @Setter
    @Column(nullable = false)
    private String nombre;

    @Getter
    @Setter
    @Column
    private String tamano;

    @Column
    @Setter
    @Getter
    private String color;

    @Setter
    @Getter
    @Column
    private LocalDate fecha;

    @Setter
    @Getter
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = true)
    private Ubicacion ubicacion;


    @Setter
    @Getter
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = true)
    private Estado estado;

    @Getter
    @Setter
    @Column(name = "descripcion_extra", length = 500)
    private String descripcionExtra;

    public Mascota() {
    }

    public Mascota(Usuario usuario, String nombre,
                   String tamano, String color, LocalDate fecha,
                   Ubicacion ubicacion, Estado estado, String descripcionExtra) {
        this.usuario = usuario;
        this.avistamientos = new ArrayList<>();
        this.nombre = nombre;
        this.tamano = tamano;
        this.color = color;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.descripcionExtra = descripcionExtra;
    }

}
