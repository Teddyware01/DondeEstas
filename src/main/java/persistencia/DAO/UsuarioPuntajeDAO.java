package persistencia.DAO;



import dondeestas.entity.Puntaje;
import dondeestas.entity.Usuario;
import dondeestas.entity.UsuarioPuntaje;

import java.util.List;

public interface UsuarioPuntajeDAO extends GenericDAO<UsuarioPuntaje>{
    List<UsuarioPuntaje> findByUsuario(Long idUsuario);
    Integer getTotalPuntosByUsuario(Long idUsuario);
    List<UsuarioPuntaje> findByTipo(Puntaje tipoPuntaje);
    List<Usuario> findTopUsuariosByPuntaje(int limit); // para ranking

}
