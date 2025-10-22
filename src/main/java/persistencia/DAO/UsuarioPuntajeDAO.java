package persistencia.DAO;



import dondeestas.Puntaje;
import dondeestas.Usuario;
import dondeestas.UsuarioPuntaje;

import java.util.List;

public interface UsuarioPuntajeDAO extends GenericDAO<UsuarioPuntaje>{
    List<UsuarioPuntaje> findByUsuario(Long idUsuario);
    Integer getTotalPuntosByUsuario(Long idUsuario);
    List<UsuarioPuntaje> findByTipo(Puntaje tipoPuntaje);
    List<Usuario> findTopUsuariosByPuntaje(int limit); // para ranking

}
