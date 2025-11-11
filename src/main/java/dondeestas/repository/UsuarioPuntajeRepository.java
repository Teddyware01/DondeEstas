
package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.UsuarioPuntaje;

@Repository
public interface UsuarioPuntajeRepository extends JpaRepository<UsuarioPuntaje, Long> {
}
