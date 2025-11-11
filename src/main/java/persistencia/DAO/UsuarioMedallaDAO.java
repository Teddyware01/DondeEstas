package persistencia.DAO;



import dondeestas.entity.Medalla;
import dondeestas.entity.UsuarioMedalla;

import java.util.List;

public interface UsuarioMedallaDAO extends GenericDAO<UsuarioMedalla>{
    List<UsuarioMedalla> findByUsuario(Long idUsuario);
    boolean usuarioTieneMedalla(Long idUsuario, Medalla tipoMedalla);
    List<UsuarioMedalla> findByTipo(Medalla tipoMedalla);
}
