package dondeestas.service;

import dondeestas.auxClass.EstadoEnum;
import dondeestas.entity.Mascota;
import dondeestas.entity.MascotaImagen;
import dondeestas.repository.MascotaRepository;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    public MascotaService(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    @Transactional
    public Mascota registrarMascota(Mascota mascota, List<MultipartFile> archivos) throws IOException, IOException {
        // Agregar imágenes si vienen archivos
        if (archivos != null) {
            for (MultipartFile file : archivos) {
                String base64 = Base64.getEncoder().encodeToString(file.getBytes());
                MascotaImagen imagen = new MascotaImagen(mascota, base64);
                mascota.getImagenes().add(imagen);
            }
        }

        // Guardar la mascota con sus imágenes
        return mascotaRepository.save(mascota);
    }

    public Mascota registrarMascota(Mascota mascota) { return mascotaRepository.save(mascota); }

    public Optional<Mascota> buscarPorId(Long id) {
        return mascotaRepository.findById(id);
    }

    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }

    public List<Mascota> buscarPorUsuario(Long idUsuario) {
        return mascotaRepository.findByUsuarioId(idUsuario);
    }



    public List<Mascota> buscarPorNombreExacto(String nombre) {
        return mascotaRepository.findByNombre(nombre);
    }

    public List<Mascota> buscarPorNombreContiene(String cadena) {
        return mascotaRepository.findByNombreContainingIgnoreCase(cadena);
    }
    @Transactional
    public Mascota actualizarMascota(Long id, Mascota nuevaMascota) {
        return mascotaRepository.findById(id)
                .map(existente -> {
                    if (nuevaMascota.getNombre() != null) {
                        existente.setNombre(nuevaMascota.getNombre());
                    }
                    if (nuevaMascota.getTamano() != null) {
                        existente.setTamano(nuevaMascota.getTamano());
                    }
                    if (nuevaMascota.getColor() != null) {
                        existente.setColor(nuevaMascota.getColor());
                    }
                    if (nuevaMascota.getUsuario() != null) {
                        existente.setUsuario(nuevaMascota.getUsuario());
                    }
                    if (nuevaMascota.getEstado() != null) {
                        existente.setEstado(nuevaMascota.getEstado());
                    }
                    if (nuevaMascota.getDescripcionExtra() != null) {
                        existente.setDescripcionExtra(nuevaMascota.getDescripcionExtra());
                    }

                    return mascotaRepository.save(existente);
                })
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
    }


    @Transactional
    public void eliminarMascota(Long id) {
        mascotaRepository.deleteById(id);
    }

    public List<Mascota> listarMascotasPerdidas() {
        return mascotaRepository.findByEstadoIn(
                List.of(EstadoEnum.PERDIDO_PROPIO, EstadoEnum.PERDIDO_AJENO)
        );
    }

    public List<Mascota> listarMascotasEncontradas() {
        return mascotaRepository.findByEstado(EstadoEnum.RECUPERADO);
    }

    public static List<Mascota> filtrarPorDistancia(List<Mascota> mascotas, double lat, double lon, double maxKm) {
        List<Mascota> filtradas = new ArrayList<>();

        for (Mascota m : mascotas) {
            if (m.getLatitud() == null || m.getLongitud() == null) continue;

            GeodesicData result = Geodesic.WGS84.Inverse(lat, lon, m.getLatitud(), m.getLongitud());
            double distanceKm = result.s12 / 1000.0; // convertir metros a km

            if (distanceKm <= maxKm) {
                filtradas.add(m);
            }
        }

        return filtradas;
    }

    public List<Mascota> listarMascotasPerdidasAjenas(){
        return mascotaRepository.findByEstado(EstadoEnum.PERDIDO_AJENO);

    }
    public List<Mascota> listarMascotasPerdidasPropias(){
        return mascotaRepository.findByEstado(EstadoEnum.PERDIDO_PROPIO);
    }


    public List<Mascota> buscarPorEstado(EstadoEnum estado) {
        return mascotaRepository.findByEstado(estado);
    }
}
