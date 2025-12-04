package dondeestas.service;

import dondeestas.entity.Usuario;
import dondeestas.repository.UsuarioPuntajeRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioPuntajeService {

    private final UsuarioPuntajeRepository usuarioPuntajeRepository;

    public UsuarioPuntajeService(UsuarioPuntajeRepository usuarioPuntajeRepository) {
        this.usuarioPuntajeRepository = usuarioPuntajeRepository;
    }

    public int obtenerTotalPuntos(Usuario usuario) {
        return usuarioPuntajeRepository.totalPuntosPorUsuario(usuario);
    }
}
