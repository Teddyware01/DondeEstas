package persistencia.DAO;


import dondeestas.Avistamiento;
import dondeestas.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface AvistamientoDAO extends GenericDAO<Avistamiento>{
    List<Avistamiento> findByMascota(Long idMascota);
    List<Avistamiento> findByUsuario(Long idUsuario);
    List<Avistamiento> findByFecha(LocalDate fecha);
    List<Avistamiento> findByBarrio(String barrio);
}
