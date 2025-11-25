package dondeestas.service;

import dondeestas.entity.Estado;
import dondeestas.entity.Mascota;
import dondeestas.repository.MascotaRepository;
import dondeestas.repository.UsuarioRepository;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.NoSuchElementException;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    public MascotaService(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    public Mascota registrarMascota(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    public Optional<Mascota> buscarPorId(Long id) {
        return mascotaRepository.findById(id);
    }

    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }

    public List<Mascota> buscarPorUsuario(Long idUsuario) {
        return mascotaRepository.findByUsuarioId(idUsuario);
    }

    public List<Mascota> buscarPorEstado(Estado estado) {
        return mascotaRepository.findByEstado(estado);
    }

    public List<Mascota> buscarPorBarrio(String barrio) {
        return mascotaRepository.findByUbicacion_BarrioIgnoreCase(barrio);
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
                    if (nuevaMascota.getUbicacion() != null) {
                        existente.setUbicacion(nuevaMascota.getUbicacion());
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
        return mascotaRepository.findByEstado_NombreEstadoStartingWithIgnoreCase("PERDIDO");
    }

    public List<Mascota> listarMascotasEncontradas() {
        return mascotaRepository.findByEstado_NombreEstadoStartingWithIgnoreCase("ENCONTRADO");
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
        //TODO
        //Falta corregir implementacion de estados
        return null;
    }
    public List<Mascota> listarMascotasPerdidasPropias(){
        //TODO
        //Falta corregir implementacion de estados
        return null;
    }
}
