package persistencia.hibernate;

import dondeestas.Puntaje;
import dondeestas.Usuario;
import dondeestas.UsuarioPuntaje;
import jakarta.persistence.EntityManager;
import persistencia.DAO.UsuarioPuntajeDAO;
import persistencia.EMF;

import java.util.List;

public class UsuarioPuntajeDAOHibernateJPA extends GenericDAOHibernateJPA<UsuarioPuntaje>
        implements UsuarioPuntajeDAO {


    public UsuarioPuntajeDAOHibernateJPA() {
        super(UsuarioPuntaje.class);
    }

    @Override
    public List<UsuarioPuntaje> findByUsuario(Long idUsuario) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<UsuarioPuntaje> resultado = null;

        try {
            resultado = em.createQuery(
                            "SELECT up FROM UsuarioPuntaje up WHERE up.usuario.id = :idUsuario",
                            UsuarioPuntaje.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

    @Override
    public Integer getTotalPuntosByUsuario(Long idUsuario) {
        EntityManager em = EMF.getEMF().createEntityManager();
        Integer total = 0;

        try {
            Long suma = em.createQuery(
                            "SELECT SUM(up.puntaje.cantidad) FROM UsuarioPuntaje up WHERE up.usuario.id = :idUsuario",
                            Long.class)
                    .setParameter("idUsuario", idUsuario)
                    .getSingleResult();

            total = (suma != null) ? suma.intValue() : 0;
        } finally {
            em.close();
        }

        return total;
    }

    @Override
    public List<UsuarioPuntaje> findByTipo(Puntaje tipoPuntaje) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<UsuarioPuntaje> resultado = null;

        try {
            resultado = em.createQuery(
                            "SELECT up FROM UsuarioPuntaje up WHERE up.puntaje = :tipo",
                            UsuarioPuntaje.class)
                    .setParameter("tipo", tipoPuntaje)
                    .getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

    @Override
    public List<Usuario> findTopUsuariosByPuntaje(int limit) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Usuario> resultado = null;

        try {
            resultado = em.createQuery(
                            "SELECT up.usuario FROM UsuarioPuntaje up " +
                                    "GROUP BY up.usuario " +
                                    "ORDER BY SUM(up.puntaje.cantidad) DESC",
                            Usuario.class)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }


}