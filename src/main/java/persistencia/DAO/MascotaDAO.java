package persistencia.DAO;


import dondeestas.Estado;
import dondeestas.Mascota;
import dondeestas.Usuario;

import java.util.List;

public interface MascotaDAO extends GenericDAO<Mascota>{
    List<Mascota> findByEstado(Estado estado);
    List<Mascota> findByUsuario(Long idUsuario); // mascotas creadas por un usuario
    List<Mascota> findByBarrio(String barrio);   // usando la ubicacion
    List<Mascota> searchByNombreExacto(String nombre); // para buscador
    List<Mascota> searchByNombreContains(String cadena); // para buscador
}
