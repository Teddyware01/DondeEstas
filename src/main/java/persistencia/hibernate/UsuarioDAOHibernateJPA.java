package persistencia.hibernate;

import dondeestas.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import persistencia.hibernate.GenericDAOHibernateJPA;
import persistencia.DAO.UsuarioDAO;
import persistencia.EMF;

import java.util.List;

public class UsuarioDAOHibernateJPA extends GenericDAOHibernateJPA<Usuario>
        implements UsuarioDAO {


    public UsuarioDAOHibernateJPA(EntityManager em) {
        super(Usuario.class,em);
    }



    public Usuario getByEmail(String mail) {
        try {
            return em.createQuery("SELECT u FROM " +
                            this.getPersistentClass().getSimpleName() + " u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", mail).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
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
        try {
            return em.createQuery("SELECT u FROM " +
                            this.getPersistentClass().getSimpleName() + " u WHERE u.barrio = :barrio", Usuario.class)
                    .setParameter("barrio", barrio).getResultList();
        } finally {
            em.close();
        }
    }
}