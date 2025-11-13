package dondeestas.repository;

import dondeestas.entity.Avistamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {
    List<Avistamiento> findByMascotaId(Long mascotaId);

    List<Avistamiento> findByUsuarioId(Long usuarioId);
}
