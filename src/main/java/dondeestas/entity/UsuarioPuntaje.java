package dondeestas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import persistencia.DAO.FactoryDAO;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_puntaje")
public class UsuarioPuntaje {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Getter
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

    public static void  verRanking(int limit) {
        FactoryDAO.getUsuarioPuntajeDAO().findTopUsuariosByPuntaje(limit);
    }


}