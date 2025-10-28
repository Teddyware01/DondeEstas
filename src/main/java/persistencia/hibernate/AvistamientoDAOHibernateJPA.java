package persistencia.hibernate;

import dondeestas.Avistamiento;
import dondeestas.Mascota;
import dondeestas.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import persistencia.DAO.AvistamientoDAO;
import persistencia.DAO.MascotaDAO;
import persistencia.EMF;

import java.time.LocalDate;
import java.util.List;

public class AvistamientoDAOHibernateJPA extends GenericDAOHibernateJPA<Avistamiento>
        implements AvistamientoDAO {


    public AvistamientoDAOHibernateJPA() {
        super(Avistamiento.class);
    }

    @Override
    public List<Avistamiento> findByMascota(Long idMascota) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> resultado = null;

        try {
            TypedQuery<Avistamiento> consulta = em.createQuery(
                    "SELECT e FROM Avistamiento e WHERE e.mascota.id = :idMascota " ,
                    Avistamiento.class
            );
            consulta.setParameter("idMascota", idMascota);
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }


    /*@Override
    public List<Avistamiento> findByUsuario(Long idUsuario) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> resultado = null;

        try {
            TypedQuery<Avistamiento> consulta = em.createQuery(
                    "SELECT e FROM Avistamiento e WHERE e.usuario.id = :idUsuario " ,
                    Avistamiento.class
            );
            consulta.setParameter("idUsuario", idUsuario);
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }
        return resultado;
    }*/

    @Override
    public List<Avistamiento> findByUsuario(Usuario usuario) { // Acepta el Objeto Usuario
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            TypedQuery<Avistamiento> consulta = em.createQuery(
                    // JPQL compara el objeto entidad completo. Esto es válido en JPA.
                    "SELECT a FROM Avistamiento a WHERE a.usuario = :usuario",
                    Avistamiento.class
            );
            consulta.setParameter("usuario", usuario); // Pasa el Objeto Usuario
            return consulta.getResultList();
        }
    }

    @Override
    public List<Avistamiento> findByFecha(LocalDate fecha) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> resultado = null;

        try {
            TypedQuery<Avistamiento> consulta = em.createQuery(
                    "SELECT e FROM Avistamiento e WHERE e.fecha = :fecha ",
                    Avistamiento.class
            );
            consulta.setParameter("fecha", fecha);
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

    @Override
    public List<Avistamiento> findByBarrio(String barrio) {

        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> resultado = null;

        try {
            TypedQuery<Avistamiento> consulta = em.createQuery(
                    "SELECT e FROM Avistamiento e WHERE LOWER(e.ubicacion.barrio) = LOWER(:barrio) "  ,
                    Avistamiento.class
            );
            consulta.setParameter("barrio", barrio);
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Avistamiento av = em.find(Avistamiento.class, id);
            if (av != null) {
                em.remove(av);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}