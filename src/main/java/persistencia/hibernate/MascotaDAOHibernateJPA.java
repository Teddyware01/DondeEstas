package persistencia.hibernate;

import dondeestas.Avistamiento;
import dondeestas.Estado;
import dondeestas.Mascota;
import dondeestas.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import persistencia.DAO.MascotaDAO;
import persistencia.DAO.UsuarioDAO;
import persistencia.EMF;

import java.util.List;

public class MascotaDAOHibernateJPA extends GenericDAOHibernateJPA<Mascota>
        implements MascotaDAO {


    public MascotaDAOHibernateJPA() {
        super(Mascota.class);
    }


    @Override
    public List<Mascota> findByEstado(Estado estado) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> resultado = null;

        try {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.estado = :estado " ,
                    Mascota.class
            );
            consulta.setParameter("estado", estado);
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }
    @Override
    public List<Mascota> findByUsuario(Long idUsuario) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> resultado = null;

        try {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.usuario.id = :idUsuario " ,
                    Mascota.class
            );
            consulta.setParameter("idUsuario", idUsuario);
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }



    @Override
    public List<Mascota> findByBarrio(String barrio) {

        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> resultado = null;

        try {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.ubicacion.barrio = :barrio "  ,
                    Mascota.class
            );
            consulta.setParameter("barrio", barrio);
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

    @Override
    public List<Mascota> searchByNombreExacto(String nombre) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> resultado = null;

        try {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.nombre = :nombre "  ,
                    Mascota.class
            );
            consulta.setParameter("nombre", nombre);
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

    @Override
    public List<Mascota> searchByNombreContains(String cadena) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> resultado = null;

        try {
            TypedQuery<Mascota> consulta = em.createQuery(
                    "SELECT e FROM Mascota e WHERE e.nombre LIKE :cadena",
                    Mascota.class
            );
            consulta.setParameter("cadena", "%" + cadena + "%");
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

}