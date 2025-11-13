
package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.Medalla;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MedallaRepository extends JpaRepository<Medalla, Long> {
}
