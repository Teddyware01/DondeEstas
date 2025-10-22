package persistencia.DAO;


import dondeestas.Usuario;

import java.util.List;

public interface UsuarioDAO extends GenericDAO<Usuario>{

    public Usuario getByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByBarrio(String barrio);
}
