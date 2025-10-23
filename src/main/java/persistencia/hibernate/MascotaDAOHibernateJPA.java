package persistencia.hibernate;

import dondeestas.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import persistencia.DAO.MascotaDAO;
import persistencia.EMF;

import java.util.List;

public class MascotaDAOHibernateJPA extends GenericDAOHibernateJPA<Mascota>
        implements MascotaDAO {

    public MascotaDAOHibernateJPA() {
        super(Mascota.class);
    }

    @Override
    public List<Mascota> findByEstado(Estado estado) {
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.estado = :estado",
                    Mascota.class
            );
            consulta.setParameter("estado", estado);
            return consulta.getResultList();
        }
    }

    @Override
    public List<Mascota> findByUsuario(Long idUsuario) {
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.usuario.id = :idUsuario",
                    Mascota.class
            );
            consulta.setParameter("idUsuario", idUsuario);
            return consulta.getResultList();
        }
    }

    @Override
    public List<Mascota> findByBarrio(String barrio){
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.ubicacion.barrio = :barrio",
                    Mascota.class
            );
            consulta.setParameter("barrio", barrio);
            return consulta.getResultList();
        }
    }

    @Override
    public List<Mascota> searchByNombreExacto(String nombre) {
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.nombre = :nombre",
                    Mascota.class
            );
            consulta.setParameter("nombre", nombre);
            return consulta.getResultList();
        }
    }

    @Override
    public List<Mascota> searchByNombreContains(String cadena) {
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.nombre LIKE :cadena",
                    Mascota.class
            );
            consulta.setParameter("cadena", "%" + cadena + "%");
            return consulta.getResultList();
        }
    }
}
