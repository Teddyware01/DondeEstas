package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.Estado;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
