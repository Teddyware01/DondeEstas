package persistencia.DAO;


import dondeestas.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario>{
    public Usuario getByEmail(String email);
}
