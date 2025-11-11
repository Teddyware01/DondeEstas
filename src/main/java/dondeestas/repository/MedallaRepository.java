
package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.Medalla;

@Repository
public interface MedallaRepository extends JpaRepository<Medalla, Long> {
}
