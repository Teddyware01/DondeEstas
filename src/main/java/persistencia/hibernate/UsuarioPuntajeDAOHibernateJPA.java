package persistencia.hibernate;

import dondeestas.UsuarioPuntaje;
import persistencia.DAO.UsuarioPuntajeDAO;

public class UsuarioPuntajeDAOHibernateJPA extends GenericDAOHibernateJPA<UsuarioPuntaje>
        implements UsuarioPuntajeDAO {


    public UsuarioPuntajeDAOHibernateJPA() {
        super(UsuarioPuntaje.class);
    }



}