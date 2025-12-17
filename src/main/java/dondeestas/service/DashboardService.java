package dondeestas.service;

import dondeestas.auxClass.EstadoEnum;
import dondeestas.dto.DashboardStats;
import dondeestas.repository.MascotaRepository;
import dondeestas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MascotaRepository mascotaRepository;

    public DashboardStats obtenerEstadisticas() {
        
        long usuarios = usuarioRepository.count();
        long activas = mascotaRepository.findByEstadoIn(List.of(EstadoEnum.PERDIDO_PROPIO,EstadoEnum.PERDIDO_AJENO)).size();
        long reencuentros = mascotaRepository.findByEstado(EstadoEnum.RECUPERADO).size();
        long barrios = mascotaRepository.countMunicipiosDistintos();
        long provincias = mascotaRepository.countProvinciasDistintas();


        return new DashboardStats(usuarios, activas, reencuentros, barrios, provincias);
    }
}