package persistencia.hibernate;

import dondeestas.Puntaje;
import persistencia.DAO.PuntajeDAO;

public class PuntajeDAOHibernateJPA extends GenericDAOHibernateJPA<Puntaje>
        implements PuntajeDAO {


    public PuntajeDAOHibernateJPA() {
        super(Puntaje.class);
    }



}