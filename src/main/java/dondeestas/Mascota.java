package dondeestas;

import jakarta.persistence.*;
import persistencia.DAO.AvistamientoDAO;
import persistencia.DAO.FactoryDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public String getNombre() {
        return nombre;
    }

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

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public List<Avistamiento> getAvistamientos() {
        return avistamientos;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getDescripcionExtra() {
        return descripcionExtra;
    }

    public void setDescripcionExtra(String descripcionExtra) {
        this.descripcionExtra = descripcionExtra;
    }

    //recibe objetos instanciados, pero la "BACK REFERENCE" en ellos se actualiza aca.
    public Avistamiento crearAvistamiento(String foto, LocalDateTime fecha, String comentario, Mascota mascota, Usuario usuario, Ubicacion ubicacion) {
        Avistamiento avistamiento = new Avistamiento(foto, fecha, comentario, this, usuario, ubicacion);
        this.avistamientos.add(avistamiento);
        usuario.agregarAvistamiento(avistamiento);
        ubicacion.addAvistamiento(avistamiento);
        FactoryDAO.getAvistamientoDAO().persist(avistamiento);
        FactoryDAO.getMascotaDAO().update(this);
        FactoryDAO.getUsuarioDAO().update(usuario);
        FactoryDAO.getUbicacionDAO().update(ubicacion);
        return avistamiento;
    }

    public void actualizarEstado(Estado estado) {
        this.estado = estado;
        FactoryDAO.getEstadoDAO().update(estado);
    }


    public void borrarMascota() {
        FactoryDAO.getMascotaDAO().delete(this);
    }

    public static Mascota getMascota(Long id) {
        return FactoryDAO.getMascotaDAO().get(id);
    }


    //recibe objetos instanciados, pero la "BACK REFERENCE" en ellos se actualiza aca.
    public void crearMascota(String nombre, String tamano, String color, LocalDate fecha, Ubicacion ubicacion, Estado estado, String descripcionExtra) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.color = color;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.descripcionExtra = descripcionExtra;
        FactoryDAO.getMascotaDAO().update(this);
    }

    public List<Avistamiento> verAvistamientos() {
        return avistamientos;
    }

}


