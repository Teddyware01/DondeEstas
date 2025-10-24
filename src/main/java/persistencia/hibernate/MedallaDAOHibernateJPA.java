package persistencia.hibernate;

import dondeestas.Avistamiento;
import dondeestas.Medalla;
import jakarta.persistence.EntityManager;
import persistencia.DAO.AvistamientoDAO;
import persistencia.DAO.MedallaDAO;

public class MedallaDAOHibernateJPA extends GenericDAOHibernateJPA<Medalla>
        implements MedallaDAO {


    public MedallaDAOHibernateJPA(EntityManager em) {
        super(Medalla.class,em);
    }



}