package persistencia.DAO;


import dondeestas.entity.Mascota;

import java.util.List;

public interface MascotaDAO extends GenericDAO<Mascota>{
    List<Mascota> findByUsuario(Long idUsuario); // mascotas creadas por un usuario
    List<Mascota> searchByNombreExacto(String nombre); // para buscador
    List<Mascota> searchByNombreContains(String cadena); // para buscador
}
