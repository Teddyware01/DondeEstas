
package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.UsuarioMedalla;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UsuarioMedallaRepository extends JpaRepository<UsuarioMedalla, Long> {
}
