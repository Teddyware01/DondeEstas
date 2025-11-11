package persistencia.hibernate;

import dondeestas.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
        try {
            return em.createQuery("SELECT u FROM " +
                            this.getPersistentClass().getSimpleName() + " u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", mail).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
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
        try {
            return em.createQuery("SELECT u FROM " +
                            this.getPersistentClass().getSimpleName() + " u WHERE u.barrio = :barrio", Usuario.class)
                    .setParameter("barrio", barrio).getResultList();
        } finally {
            em.close();
        }
    }
}