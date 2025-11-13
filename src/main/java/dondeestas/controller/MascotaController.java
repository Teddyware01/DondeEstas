package dondeestas.controller;

import dondeestas.entity.Mascota;
import dondeestas.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    // Crear mascota
    @PostMapping
    public ResponseEntity<Mascota> crearMascota(@RequestBody Mascota mascota) {
        Mascota nueva = mascotaService.registrarMascota(mascota);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // Editar mascota
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> editarMascota(@PathVariable Long id,
                                                 @RequestBody Mascota mascotaActualizada) {
        Optional<Mascota> mascotaOpt = Optional.ofNullable(mascotaService.actualizarMascota(id, mascotaActualizada));
        if (mascotaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Mascota mascota = mascotaOpt.get();
        mascota.setNombre(mascotaActualizada.getNombre());
        mascota.setEstado(mascotaActualizada.getEstado());
        // otros campos según tu entity

        Mascota guardada = mascotaService.registrarMascota(mascota);
        return ResponseEntity.ok(guardada);
    }

    // Eliminar mascota
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
        return ResponseEntity.noContent().build();
    }

    // Ver todas las mascotas de un usuario
    @GetMapping("/usuario/{idUsuario}")
    public List<Mascota> listarMascotasPorUsuario(@PathVariable Long idUsuario) {
        return mascotaService.listarMascotasPorUsuario(idUsuario);
    }

    // Listado de todas las mascotas perdidas
    @GetMapping("/perdidas")
    public List<Mascota> listarMascotasPerdidas() {
        return mascotaService.listarMascotasPerdidas();
    }
    // Listado de todas las mascotas encontradas
    @GetMapping("/encontradas")
    public List<Mascota> listarMascotasEncontradas() {
        return mascotaService.listarMascotasEncontradas();
    }

}
