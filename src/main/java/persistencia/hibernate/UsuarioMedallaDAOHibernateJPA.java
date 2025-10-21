package persistencia.hibernate;

import dondeestas.UsuarioMedalla;
import persistencia.DAO.UsuarioMedallaDAO;

public class UsuarioMedallaDAOHibernateJPA extends GenericDAOHibernateJPA<UsuarioMedalla>
        implements UsuarioMedallaDAO {


    public UsuarioMedallaDAOHibernateJPA() {
        super(UsuarioMedalla.class);
    }



}