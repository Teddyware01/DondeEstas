package persistencia.hibernate;

import dondeestas.entity.Ubicacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import persistencia.DAO.UbicacionDAO;
import persistencia.EMF;

import java.util.List;

public class UbicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Ubicacion>
        implements UbicacionDAO {


    public UbicacionDAOHibernateJPA() {
        super(Ubicacion.class);
    }


    @Override
    public List<Ubicacion> findByBarrio(String barrio) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Ubicacion> resultado = null;

        try {
            TypedQuery<Ubicacion> consulta = em.createQuery(
                    "SELECT u FROM Ubicacion u WHERE u.barrio LIKE :barrio",
                    Ubicacion.class
            );
            consulta.setParameter("barrio", "%" + barrio + "%");
            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

    //Interesante de ver, podemos probarlo.
    @Override
    public List<Ubicacion> findWithinRadio(double lat, double lon, double radioKm) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Ubicacion> resultado = null;

        try {
            TypedQuery<Ubicacion> consulta = em.createQuery(
                    "SELECT u FROM Ubicacion u WHERE " +
                            "(6371 * acos(" +
                            "cos(radians(:lat)) * cos(radians(u.latitud)) * " +
                            "cos(radians(u.longitud) - radians(:lon)) + " +
                            "sin(radians(:lat)) * sin(radians(u.latitud))" +
                            ")) <= :radio",
                    Ubicacion.class
            );
            consulta.setParameter("lat", lat);
            consulta.setParameter("lon", lon);
            consulta.setParameter("radio", radioKm);

            resultado = consulta.getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }



}