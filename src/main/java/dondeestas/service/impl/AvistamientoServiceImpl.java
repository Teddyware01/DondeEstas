package dondeestas.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import dondeestas.repository.AvistamientoRepository;
import dondeestas.service.AvistamientoService;

@Service
public class AvistamientoServiceImpl implements AvistamientoService {

    private final AvistamientoRepository avistamientoRepository;

    @Autowired
    public AvistamientoServiceImpl(AvistamientoRepository avistamientoRepository) {
        this.avistamientoRepository = avistamientoRepository;
    }

    // Implementaciones de los métodos irán aquí
}
