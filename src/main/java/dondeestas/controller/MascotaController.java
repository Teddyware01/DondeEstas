package dondeestas.controller;

import dondeestas.entity.Mascota;
import dondeestas.service.MascotaService;
import jakarta.validation.Valid;
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
    public List<Mascota> listarMascotasPorUsuario(@PathVariable Long idUsuario) {
        return mascotaService.listarMascotasPorUsuario(idUsuario);
    }

    @GetMapping("/encontradas")
    public ResponseEntity<List<Mascota>> listarMascotasEncontradas() {
        List<Mascota> perdidas = mascotaService.listarMascotasEncontradas();
        if (perdidas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(perdidas, HttpStatus.OK);
    }

}
