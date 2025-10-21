package persistencia.hibernate;

import dondeestas.Avistamiento;
import dondeestas.Mascota;
import persistencia.DAO.AvistamientoDAO;
import persistencia.DAO.MascotaDAO;

public class AvistamientoDAOHibernateJPA extends GenericDAOHibernateJPA<Avistamiento>
        implements AvistamientoDAO {


    public AvistamientoDAOHibernateJPA() {
        super(Avistamiento.class);
    }



}