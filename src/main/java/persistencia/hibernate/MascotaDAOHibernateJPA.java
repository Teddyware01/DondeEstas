package persistencia.hibernate;

import dondeestas.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import persistencia.DAO.MascotaDAO;

import java.util.List;

public class MascotaDAOHibernateJPA extends GenericDAOHibernateJPA<Mascota>
        implements MascotaDAO {

    private final EntityManager em;

    public MascotaDAOHibernateJPA(EntityManager em) {
        super(Mascota.class);
        this.em = em;
    }

    @Override
    public List<Mascota> findByEstado(Estado estado) {
        TypedQuery<Mascota> consulta = em.createQuery(
                "SELECT e FROM Mascota e WHERE e.estado = :estado",
                Mascota.class
        );
        consulta.setParameter("estado", estado);
        return consulta.getResultList();
    }

    @Override
    public List<Mascota> findByUsuario(Long idUsuario) {
        TypedQuery<Mascota> consulta = em.createQuery(
                "SELECT e FROM Mascota e WHERE e.usuario.id = :idUsuario",
                Mascota.class
        );
        consulta.setParameter("idUsuario", idUsuario);
        return consulta.getResultList();
    }

    @Override
    public List<Mascota> findByBarrio(String barrio) {
        TypedQuery<Mascota> consulta = em.createQuery(
                "SELECT e FROM Mascota e WHERE e.ubicacion.barrio = :barrio",
                Mascota.class
        );
        consulta.setParameter("barrio", barrio);
        return consulta.getResultList();
    }

    @Override
    public List<Mascota> searchByNombreExacto(String nombre) {
        TypedQuery<Mascota> consulta = em.createQuery(
                "SELECT e FROM Mascota e WHERE e.nombre = :nombre",
                Mascota.class
        );
        consulta.setParameter("nombre", nombre);
        return consulta.getResultList();
    }

    @Override
    public List<Mascota> searchByNombreContains(String cadena) {
        TypedQuery<Mascota> consulta = em.createQuery(
                "SELECT e FROM Mascota e WHERE e.nombre LIKE :cadena",
                Mascota.class
        );
        consulta.setParameter("cadena", "%" + cadena + "%");
        return consulta.getResultList();
    }

}
