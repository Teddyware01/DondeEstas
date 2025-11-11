package persistencia.hibernate;

import dondeestas.entity.Estado;
import persistencia.DAO.EstadoDAO;

public class EstadoDAOHibernateJPA extends GenericDAOHibernateJPA<Estado>
        implements EstadoDAO {


    public EstadoDAOHibernateJPA() {
        super(Estado.class);
    }



}