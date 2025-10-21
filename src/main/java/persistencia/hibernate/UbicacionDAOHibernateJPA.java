package persistencia.hibernate;

import dondeestas.Ubicacion;
import persistencia.DAO.UbicacionDAO;

public class UbicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Ubicacion>
        implements UbicacionDAO {


    public UbicacionDAOHibernateJPA() {
        super(Ubicacion.class);
    }



}