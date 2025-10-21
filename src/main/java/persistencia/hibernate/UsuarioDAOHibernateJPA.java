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

}