package persistencia.hibernate;

import dondeestas.Usuario;
import jakarta.persistence.EntityManager;
import persistencia.hibernate.GenericDAOHibernateJPA;
import persistencia.DAO.UsuarioDAO;
import persistencia.EMF;

import java.util.List;

public class UsuarioDAOHibernateJPA extends GenericDAOHibernateJPA<Usuario>
        implements UsuarioDAO {


    public UsuarioDAOHibernateJPA() {
        super(Usuario.class);
    }



    public Usuario getByEmail(String mail) {
        EntityManager em = EMF.getEMF().createEntityManager();
        Usuario usr;
        try {
            usr = (Usuario) em.createQuery("SELECT m FROM " +
                            this.getPersistentClass().getSimpleName() + " m WHERE m.email = :email")
                    .setParameter("email", mail).getSingleResult();
        } catch (Exception e) {
            usr = null;
        } finally {
            em.close();
        }
        return usr;
    }
    @Override
    public boolean existsByEmail(String email) {
        EntityManager em = EMF.getEMF().createEntityManager();
        boolean existe = false;

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM " + getPersistentClass().getSimpleName() + " u WHERE u.email = :email",
                            Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            existe = count > 0;
        } finally {
            em.close();
        }

        return existe;
    }

    @Override
    public List<Usuario> findByBarrio(String barrio) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Usuario> resultado = null;

        try {
            resultado = em.createQuery(
                            "SELECT u FROM " + getPersistentClass().getSimpleName() + " u WHERE u.barrio = :barrio",
                            Usuario.class)
                    .setParameter("barrio", barrio)
                    .getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }



}