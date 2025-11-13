package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.Ubicacion;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}
