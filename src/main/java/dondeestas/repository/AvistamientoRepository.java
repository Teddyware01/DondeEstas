package dondeestas.repository;

import dondeestas.entity.Avistamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {
}
