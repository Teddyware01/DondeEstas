package dondeestas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import persistencia.DAO.FactoryDAO;
import persistencia.hibernate.MascotaDAOHibernateJPA;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @JsonIgnore
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
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;


    @Setter
    @Getter
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
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

    public static Mascota crearYGuardar(Usuario usuario, String nombre, String tamano, String color, LocalDate fecha, Ubicacion ubicacion, Estado estado, String descripcionExtra) {
        Mascota mascota = new Mascota(usuario, nombre, tamano, color, fecha, ubicacion, estado, descripcionExtra);
        FactoryDAO.getMascotaDAO().persist(mascota);
        return mascota;
    }

    public static Mascota getMascota(Long id) {
        return FactoryDAO.getMascotaDAO().get(id);
    }

    public static void guardarMascota(Mascota mascota) {
        FactoryDAO.getMascotaDAO().update(mascota);
    }

    public static List<Mascota> findByEstado(Estado estado) {
        MascotaDAOHibernateJPA dao = (MascotaDAOHibernateJPA) FactoryDAO.getMascotaDAO();
        return dao.findByEstado(estado);
    }

    public static List<Mascota> findByUsuario(Long idUsuario) {
        MascotaDAOHibernateJPA dao = (MascotaDAOHibernateJPA) FactoryDAO.getMascotaDAO();
        return dao.findByUsuario(idUsuario);
    }

    public static List<Mascota> findByBarrio(String barrio) {
        MascotaDAOHibernateJPA dao = (MascotaDAOHibernateJPA) FactoryDAO.getMascotaDAO();
        return dao.findByBarrio(barrio);
    }

    public static List<Mascota> searchByNombreExacto(String nombre) {
        MascotaDAOHibernateJPA dao = (MascotaDAOHibernateJPA) FactoryDAO.getMascotaDAO();
        return dao.searchByNombreExacto(nombre);
    }

    public static List<Mascota> searchByNombreContains(String cadena) {
        MascotaDAOHibernateJPA dao = (MascotaDAOHibernateJPA) FactoryDAO.getMascotaDAO();
        return dao.searchByNombreContains(cadena);
    }

    public void borrarMascota() {
        FactoryDAO.getMascotaDAO().delete(this);
    }

    public void actualizarEstado(Estado estado) {
        this.estado = estado;
        FactoryDAO.getMascotaDAO().update(this);
    }

    public Avistamiento registrarAvistamiento(String foto, LocalDateTime fecha, String comentario, Usuario usuario, Ubicacion ubicacion) {
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

    public List<Avistamiento> verAvistamientos() {
        return avistamientos;
    }
}
