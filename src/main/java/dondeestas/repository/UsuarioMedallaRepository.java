
package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.UsuarioMedalla;

@Repository
public interface UsuarioMedallaRepository extends JpaRepository<UsuarioMedalla, Long> {
}
