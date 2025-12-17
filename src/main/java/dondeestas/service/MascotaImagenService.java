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
        // Buscamos la mascota
        Mascota mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + mascotaId));

        // Creamos la nueva imagen
        MascotaImagen imagen = new MascotaImagen(mascota, base64);

        // Añadimos a la lista de la mascota
        mascota.getImagenes().add(imagen);

        // Guardamos la imagen (CascadeType.ALL de la mascota también funcionaría)
        return imagenRepository.save(imagen);
    }
}
