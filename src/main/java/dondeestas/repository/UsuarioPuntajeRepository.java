
package dondeestas.repository;

import dondeestas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dondeestas.entity.UsuarioPuntaje;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UsuarioPuntajeRepository extends JpaRepository<UsuarioPuntaje, Long> {

    @Query("SELECT COALESCE(SUM(up.puntaje.cantidad), 0) " +
            "FROM UsuarioPuntaje up " +
            "WHERE up.usuario = :usuario")
    int totalPuntosPorUsuario(@Param("usuario") Usuario usuario);
}
