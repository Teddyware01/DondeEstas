package persistencia.hibernate;

import dondeestas.entity.Medalla;
import persistencia.DAO.MedallaDAO;

public class MedallaDAOHibernateJPA extends GenericDAOHibernateJPA<Medalla>
        implements MedallaDAO {


    public MedallaDAOHibernateJPA() {
        super(Medalla.class);
    }



}