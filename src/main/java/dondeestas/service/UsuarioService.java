package dondeestas.service;

import dondeestas.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario registrar(Usuario usuario);
    Usuario editarPerfil(Long id, Usuario datos);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> login(String email, String password);
    List<Usuario> listarTodos();
    void eliminar(Long id);
    Optional<Usuario> buscarPorEmail(String email);
}
