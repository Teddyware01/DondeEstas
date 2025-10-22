package dondeestas;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avistamiento> avistamientos;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String tamano;

    @Column
    private String color;

    @Column
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @Column(length = 500)
    private String descripcionExtra;

    public Mascota(Usuario usuario, String luna, String pequeño, LocalDate now, Ubicacion ubicacion, Estado estado, String s) {
    }


    public String getNombre(){
        return nombre;
    }

    public Mascota() {}

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

    public Long getId() {
        return id;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getTamano() {
        return tamano;
    }
}
