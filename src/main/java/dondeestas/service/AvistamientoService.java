package dondeestas.service;

import dondeestas.entity.Avistamiento;
import dondeestas.entity.Mascota;
import dondeestas.entity.Usuario;
import dondeestas.entity.Ubicacion;
import dondeestas.repository.AvistamientoRepository;
import dondeestas.repository.MascotaRepository;
import dondeestas.repository.UsuarioRepository;
import dondeestas.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AvistamientoService {

    @Autowired
    private AvistamientoRepository avistamientoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Transactional // Garantiza que la operación es atómica
    public Avistamiento registrar(Avistamiento avistamiento) {

        Avistamiento nuevoAvistamiento = avistamientoRepository.save(avistamiento);
        return nuevoAvistamiento;
    }

    public Optional<Avistamiento> buscarPorId(Long id) {
        // findById retorna un Optional, estándar de Spring Data
        return avistamientoRepository.findById(id);
    }

    public List<Avistamiento> buscarPorMascota(Long mascotaId) {
        return avistamientoRepository.findByMascotaId(mascotaId);
    }

    public List<Avistamiento> buscarPorUsuario(Long usuarioId) {
        // Asumiendo que definiste el método en la interfaz AvistamientoRepository:
        // List<Avistamiento> findByUsuarioId(Long usuarioId);
        return avistamientoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void eliminar(Long id) {
        Optional<Avistamiento> avistamientoOpt = avistamientoRepository.findById(id);

        if (avistamientoOpt.isPresent()) {
            Avistamiento av = avistamientoOpt.get();

            Mascota mascotaAsociada = av.getMascota();
            if (mascotaAsociada != null) {
                mascotaAsociada.getAvistamientos().remove(av);
                mascotaRepository.save(mascotaAsociada);
            }
            Usuario usuarioAsociado = av.getUsuario();
            if (usuarioAsociado != null) {
                usuarioAsociado.getAvistamientos().remove(av);
                usuarioRepository.save(usuarioAsociado);
            }

            Ubicacion ubicacionAsociada = av.getUbicacion();
            if (ubicacionAsociada != null) {
                ubicacionAsociada.getAvistamientos().remove(av);
                ubicacionRepository.save(ubicacionAsociada);
            }

            // 2. Ejecutar la baja. Spring Data gestiona la transacción.
            avistamientoRepository.delete(av);
        } else {
            // Manejo de la excepción si el avistamiento no existe (puedes lanzar una excepción 404)
            throw new IllegalArgumentException("Avistamiento con ID " + id + " no encontrado.");
        }
    }

    @Transactional
    public Optional<Avistamiento> actualizar(Long id, Avistamiento datosActualizados) {
        return avistamientoRepository.findById(id).map(avistamientoExistente -> {
            avistamientoExistente.setComentario(datosActualizados.getComentario());
            avistamientoExistente.setFecha(datosActualizados.getFecha());
            // etc...

            // Spring Data save() funciona como persist (si es nuevo) o merge (si existe)
            return avistamientoRepository.save(avistamientoExistente);
        });
    }
}