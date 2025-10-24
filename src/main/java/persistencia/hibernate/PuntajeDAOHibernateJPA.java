package persistencia.hibernate;

import dondeestas.Puntaje;
import jakarta.persistence.EntityManager;
import persistencia.DAO.PuntajeDAO;

public class PuntajeDAOHibernateJPA extends GenericDAOHibernateJPA<Puntaje>
        implements PuntajeDAO {


    public PuntajeDAOHibernateJPA(EntityManager em) {
        super(Puntaje.class,em);
    }



}