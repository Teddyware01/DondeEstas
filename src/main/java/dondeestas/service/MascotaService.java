package dondeestas.service;

import dondeestas.entity.Estado;
import dondeestas.entity.Mascota;
import dondeestas.repository.MascotaRepository;
import dondeestas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.NoSuchElementException;
import java.util.Optional;

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
}
