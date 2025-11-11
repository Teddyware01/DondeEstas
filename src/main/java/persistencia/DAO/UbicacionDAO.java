package persistencia.DAO;


import dondeestas.entity.Ubicacion;

import java.util.List;

public interface UbicacionDAO extends GenericDAO<Ubicacion>{
    List<Ubicacion> findByBarrio(String barrio);
    // Algo asi  para busquedas geograficas:
    List<Ubicacion> findWithinRadio(double lat, double lon, double radioKm);
}
