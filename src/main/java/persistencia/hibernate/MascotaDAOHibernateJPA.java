package persistencia.hibernate;

import dondeestas.Mascota;
import dondeestas.Usuario;
import jakarta.persistence.EntityManager;
import persistencia.DAO.MascotaDAO;
import persistencia.DAO.UsuarioDAO;
import persistencia.EMF;

public class MascotaDAOHibernateJPA extends GenericDAOHibernateJPA<Mascota>
        implements MascotaDAO {


    public MascotaDAOHibernateJPA() {
        super(Mascota.class);
    }



}