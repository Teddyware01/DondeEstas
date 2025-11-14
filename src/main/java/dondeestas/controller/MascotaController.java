package dondeestas.controller;

import dondeestas.entity.Mascota;
import dondeestas.entity.Usuario;
import dondeestas.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
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
    public ResponseEntity<List<Mascota>> listarMascotasPorUsuario(@PathVariable Long idUsuario) {
        List<Mascota> mascotas = mascotaService.buscarPorUsuario(idUsuario);
        if (mascotas.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(mascotas);
    }

    // Ver UNA mascota segun su id mascotas de un usuario
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerMascotaPorId(@PathVariable Long id) {
        try {
            Mascota mascota = mascotaService.buscarPorId(id);
            return ResponseEntity.ok(mascota); // 200 OK
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // 404 NOT FOUND
        }
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

    // Listado de todas las mascotas del sistema
    @GetMapping("/todas")
    public List<Mascota> listarMascotas() {
        return mascotaService.listarTodas();
    }

    // Listado de todas las mascotas del sistema
    @GetMapping("/todas2")
    public ResponseEntity<List<Mascota>> listarMascotas2() {
        List<Mascota> mascotas = mascotaService.listarTodas();
        if (mascotas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(mascotas);

    }


}
