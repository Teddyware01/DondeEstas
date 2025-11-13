package dondeestas.repository;

import dondeestas.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.Mascota;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByUsuarioId(Long idUsuario);

    List<Mascota> findByEstado(Estado estado);

    List<Mascota> findByUbicacion_BarrioIgnoreCase(String barrio);

    List<Mascota> findByNombre(String nombre);

    List<Mascota> findByNombreContainingIgnoreCase(String cadena);

    List<Mascota> findByEstado_NombreEstadoStartingWithIgnoreCase(String prefijo);

}
