package dondeestas.service;

import dondeestas.auxClass.RankingItem;
import dondeestas.entity.Usuario;
import dondeestas.entity.UsuarioMedalla;
import dondeestas.entity.UsuarioPuntaje;
import dondeestas.repository.PuntajeRepository;
import dondeestas.repository.UsuarioPuntajeRepository;
import dondeestas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankingService {

    private final UsuarioPuntajeService usuarioPuntajeService;
    private final PuntajeRepository puntajeRepository;
    private final UsuarioService usuarioService;
    private final UsuarioMedallaService usuarioMedallaService;

    @Autowired
    public RankingService(PuntajeRepository puntajeRepository, UsuarioService usuarioService, UsuarioPuntajeService usuarioPuntajeService, UsuarioMedallaService usuarioMedallaService) {
        this.usuarioPuntajeService = usuarioPuntajeService;
        this.puntajeRepository = puntajeRepository;
        this.usuarioService = usuarioService;
        this.usuarioMedallaService = usuarioMedallaService;
    }

    public List<RankingItem> obtenerRankingCompleto() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        List<RankingItem> rankingItems = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            int total = usuarioPuntajeService.obtenerTotalPuntos(usuario);
            int cantMedallas = usuarioMedallaService.cantMedallasUsuario(usuario);
            RankingItem item = new RankingItem(usuario, total, cantMedallas);
            rankingItems.add(item);
        }

        // Ordenar de mayor a menor según total de puntos
        rankingItems.sort(Comparator.comparingInt(RankingItem::getTotalPuntos).reversed());

        return rankingItems;
    }

    public List<RankingItem> obtenerRankingLimit(int limit) {
        List<RankingItem> ranking =  obtenerRankingCompleto();
        int realLimit = Math.min(limit, ranking.size());

        return ranking.subList(0, realLimit);
    }


}
