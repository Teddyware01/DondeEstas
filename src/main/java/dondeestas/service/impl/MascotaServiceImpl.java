package dondeestas.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import dondeestas.repository.MascotaRepository;
import dondeestas.service.MascotaService;

@Service
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;

    @Autowired
    public MascotaServiceImpl(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    // Implementaciones de los métodos irán aquí
}
