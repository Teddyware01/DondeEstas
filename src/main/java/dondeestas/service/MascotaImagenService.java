package dondeestas.service;

import dondeestas.entity.Mascota;
import dondeestas.entity.MascotaImagen;
import dondeestas.repository.MascotaRepository;
import dondeestas.repository.MascotaImagenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MascotaImagenService {

    private final MascotaRepository mascotaRepository;
    private final MascotaImagenRepository imagenRepository;

    @Transactional
    public MascotaImagen agregarImagen(Long mascotaId, String base64) {
        Mascota mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + mascotaId));

        MascotaImagen imagen = new MascotaImagen(mascota, base64);

        mascota.getImagenes().add(imagen);

        return imagenRepository.save(imagen);
    }
}
