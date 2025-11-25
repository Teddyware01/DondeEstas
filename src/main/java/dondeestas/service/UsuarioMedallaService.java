package dondeestas.service;

import dondeestas.auxClass.RankingItem;
import dondeestas.entity.Usuario;
import dondeestas.repository.MedallaRepository;
import dondeestas.repository.PuntajeRepository;
import dondeestas.repository.UsuarioMedallaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class UsuarioMedallaService {

    private final UsuarioMedallaRepository usuarioMedallaRepository;
    private final MedallaRepository medallaRepository;
    @Autowired
    public UsuarioMedallaService( UsuarioMedallaRepository usuarioMedallaRepository, MedallaRepository medallaRepository) {
        this.usuarioMedallaRepository = usuarioMedallaRepository;
        this.medallaRepository = medallaRepository;
    }

    public int cantMedallasUsuario(Usuario usuario) {
        return usuarioMedallaRepository.contarMedallasPorUsuario(usuario);
    }



}
