package persistencia.hibernate;

import dondeestas.Avistamiento;
import dondeestas.Medalla;
import persistencia.DAO.AvistamientoDAO;
import persistencia.DAO.MedallaDAO;

public class MedallaDAOHibernateJPA extends GenericDAOHibernateJPA<Medalla>
        implements MedallaDAO {


    public MedallaDAOHibernateJPA() {
        super(Medalla.class);
    }



}