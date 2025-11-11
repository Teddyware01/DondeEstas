package persistencia.DAO;


import dondeestas.entity.Avistamiento;
import dondeestas.entity.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface AvistamientoDAO extends GenericDAO<Avistamiento>{
    List<Avistamiento> findByMascota(Long idMascota);
    List<Avistamiento> findByUsuario(Usuario usuario);
    List<Avistamiento> findByFecha(LocalDate fecha);
    List<Avistamiento> findByBarrio(String barrio);
}
