package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.Usuario;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring la crea  automáticamente basandose en el nombre del metodo
    Optional<Usuario> findByEmail(String email);
}
