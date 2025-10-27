package dondeestas;

import jakarta.persistence.*;
import persistencia.DAO.FactoryDAO;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_puntaje")
public class UsuarioPuntaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "puntaje_id", nullable = false)
    private Puntaje puntaje;

    @Column
    private LocalDate fechaObtencion;

    public UsuarioPuntaje(Usuario usuario, Puntaje puntaje, LocalDate fechaObtencion) {
        this.puntaje = puntaje;
        this.fechaObtencion = fechaObtencion;
        this.usuario = usuario;
    }

    public UsuarioPuntaje() {

    }

    public Puntaje getPuntaje() {
        return  puntaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Long getId() {
        return id;
    }

    public static void  verRanking(int limit) {
        FactoryDAO.getUsuarioPuntajeDAO().findTopUsuariosByPuntaje(limit);
    }


}