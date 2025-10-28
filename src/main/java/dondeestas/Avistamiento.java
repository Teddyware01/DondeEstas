package dondeestas;

import jakarta.persistence.*;
import persistencia.DAO.FactoryDAO;
import persistencia.hibernate.AvistamientoDAOHibernateJPA;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public Avistamiento() {}
    public Avistamiento(String foto, LocalDateTime fecha, String comentario, Mascota mascota, Usuario usuario, Ubicacion ubicacion) {
        this.foto = foto;
        this.fecha = fecha;
        this.comentario = comentario;
        this.mascota = mascota;
        this.usuario = usuario;
        this.ubicacion = ubicacion;
    }

    public static Avistamiento crearYGuardar(String foto, LocalDateTime fecha, String comentario, Mascota mascota, Usuario usuario, Ubicacion ubicacion) {
        Avistamiento av = new Avistamiento(foto, fecha, comentario, mascota, usuario, ubicacion);
        FactoryDAO.getAvistamientoDAO().persist(av);
        return av;
    }

    public static Avistamiento getAvistamiento(Long id) {
        return FactoryDAO.getAvistamientoDAO().get(id);
    }

    public void guardarAvistamiento() {
        FactoryDAO.getAvistamientoDAO().update(this);
    }

    public void borrarAvistamiento() {
        FactoryDAO.getAvistamientoDAO().delete(this.getId());
    }


    public static List<Avistamiento> findByUsuario(Usuario usuario) {
        AvistamientoDAOHibernateJPA dao = (AvistamientoDAOHibernateJPA) FactoryDAO.getAvistamientoDAO();
        return dao.findByUsuario(usuario);
    }

    public static List<Avistamiento> findByMascota(Mascota mascota) {
        AvistamientoDAOHibernateJPA dao = (AvistamientoDAOHibernateJPA) FactoryDAO.getAvistamientoDAO();
        return dao.findByMascota(mascota.getId());
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
}
