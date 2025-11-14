package dondeestas.controller;

import dondeestas.entity.Mascota;
import dondeestas.entity.Usuario;
import dondeestas.service.MascotaService;
import jakarta.validation.Valid;
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

    @GetMapping // tiene que arrancar listando a las mascotas perdidas
    public ResponseEntity<List<Mascota>> listarMascotasPerdidas() {
        List<Mascota> perdidas = mascotaService.listarMascotasPerdidas();

        if (perdidas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(perdidas);
    }

    @PostMapping
    public ResponseEntity<Mascota> crearMascota(@Valid @RequestBody Mascota mascota) {
        Mascota nueva = mascotaService.registrarMascota(mascota);
        if  (nueva == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }else return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

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
        // ver si faltan campos agregar

        Mascota guardada = mascotaService.registrarMascota(mascota);
        return ResponseEntity.ok(guardada);
    }

    @DeleteMapping("/{id}")
    public void eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
    }

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

    @GetMapping("/encontradas")
    public ResponseEntity<List<Mascota>> listarMascotasEncontradas() {
        List<Mascota> perdidas = mascotaService.listarMascotasEncontradas();
        if (perdidas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(perdidas, HttpStatus.OK);
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
