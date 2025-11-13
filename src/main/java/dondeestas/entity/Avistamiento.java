package dondeestas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import persistencia.DAO.FactoryDAO;
import persistencia.hibernate.AvistamientoDAOHibernateJPA;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "avistamientos")
public class Avistamiento {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Getter
    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Getter
    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;

    @Column
    private String foto;


    @Setter
    @Getter    @Column(nullable = false)
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

    public long getId() {
        return id;
    }

}
