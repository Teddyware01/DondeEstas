package dondeestas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dondeestas.auxClass.EstadoEnum;
import dondeestas.auxClass.TipoAnimalEnum;
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
    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Avistamiento> avistamientos;

    @Getter
    @Setter
    @Column(nullable = false)
    private String nombre;


    @Getter
    @Setter
    @Column(nullable = false)
    private boolean activo;

    @Getter
    @Setter
    @Column
    private String tamano;

    @Setter
    @Getter
    @Column
    private String color;

    @Setter
    @Getter
    @Column(name = "fecha_perdida")
    private LocalDate fechaPerdida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private EstadoEnum estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private TipoAnimalEnum tipoAnimal;

    @Getter
    @Setter
    @Column(name = "descripcion_extra", length = 500)
    private String descripcionExtra;

    @Getter
    @Setter
    @Column(nullable = true)
    private Double latitud;

    @Getter
    @Setter
    @Column(nullable = true)
    private Double longitud;


    @Getter
    @Setter
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MascotaImagen> imagenes = new ArrayList<>();



    @Setter
    @Getter
    @Column(nullable = true)
    private String provincia;


    @Setter
    @Getter
    @Column(nullable = true)
    private String departamento;


    @Setter
    @Getter
    @Column(nullable = true)
    private String municipio;

    public Mascota() {
    }

    public Mascota(Usuario usuario, String nombre,
                   String tamano, String color, LocalDate fechaPerdida, EstadoEnum estado, TipoAnimalEnum tipoAnimal, String descripcionExtra) {
        this.usuario = usuario;
        this.avistamientos = new ArrayList<>();
        this.nombre = nombre;
        this.tamano = tamano;
        this.color = color;
        this.fechaPerdida = fechaPerdida;
        this.estado = estado;
        this.activo = true;
        this.tipoAnimal = tipoAnimal;
        this.descripcionExtra = descripcionExtra;
    }

    @Override
    public String toString() {
        return "Mascota{" +
                "id=" + id +
                ", usuarioId=" + (usuario != null ? usuario.getId() : null) +
                ", nombre='" + nombre + '\'' +
                ", tamano='" + tamano + '\'' +
                ", color='" + color + '\'' +
                ", fecha=" + fechaPerdida +
                ", descripcionExtra='" + descripcionExtra + '\'' +
                ", avistamientosCount=" + (avistamientos != null ? avistamientos.size() : 0) +
                '}';
    }

}
