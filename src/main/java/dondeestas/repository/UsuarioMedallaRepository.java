
package dondeestas.repository;

import dondeestas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dondeestas.entity.UsuarioMedalla;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UsuarioMedallaRepository extends JpaRepository<UsuarioMedalla, Long> {

    @Query("SELECT COUNT(um) FROM UsuarioMedalla um WHERE um.usuario = :usuario")
    int contarMedallasPorUsuario(@Param("usuario") Usuario usuario);

}
