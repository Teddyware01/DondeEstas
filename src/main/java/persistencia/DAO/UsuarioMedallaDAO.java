package persistencia.DAO;



import dondeestas.Medalla;
import dondeestas.Puntaje;
import dondeestas.UsuarioMedalla;
import dondeestas.UsuarioPuntaje;

import java.util.List;

public interface UsuarioMedallaDAO extends GenericDAO<UsuarioMedalla>{
    List<UsuarioMedalla> findByUsuario(Long idUsuario);
    boolean usuarioTieneMedalla(Long idUsuario, Medalla tipoMedalla);
    List<UsuarioMedalla> findByTipo(Medalla tipoMedalla);
}
