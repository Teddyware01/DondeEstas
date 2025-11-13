package dondeestas.service;

import dondeestas.entity.Usuario;
import dondeestas.repository.UsuarioRepository;
import dondeestas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public Usuario registrar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario editarPerfil(Long id, Usuario datos) {
        return usuarioRepository.findById(id)
                .map(u -> {
                    u.setNombre(datos.getNombre());
                    u.setEmail(datos.getEmail());
                    u.setContrasena(datos.getContrasena());
                    return usuarioRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(email) && u.getContrasena().equals(password))
                .findFirst();
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
