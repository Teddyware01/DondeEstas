package persistencia.hibernate;

import dondeestas.Avistamiento;
import dondeestas.Estado;
import jakarta.persistence.EntityManager;
import persistencia.DAO.AvistamientoDAO;
import persistencia.DAO.EstadoDAO;

public class EstadoDAOHibernateJPA extends GenericDAOHibernateJPA<Estado>
        implements EstadoDAO {


    public EstadoDAOHibernateJPA(EntityManager em) {
        super(Estado.class,em);
    }



}