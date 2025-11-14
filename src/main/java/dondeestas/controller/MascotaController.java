package dondeestas.controller;

import dondeestas.entity.Mascota;
import dondeestas.entity.Usuario;
import dondeestas.service.MascotaService;
import dondeestas.service.UsuarioService;
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
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public ResponseEntity<List<Mascota>> listarTodas() {
        List<Mascota> mascotas = mascotaService.listarTodas();
        if (mascotas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(mascotas);

    }

    @GetMapping("/perdidas")
    public ResponseEntity<List<Mascota>> listarMascotasPerdidas() {
        List<Mascota> perdidas = mascotaService.listarMascotasPerdidas();

        if (perdidas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(perdidas);
    }


    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Mascota> crearMascota(
            @Valid @RequestBody Mascota mascota,
            @PathVariable Long usuarioId) {

        Optional<Usuario> usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        mascota.setUsuario(usuario.get());

        Mascota nueva = mascotaService.registrarMascota(mascota);

        if (nueva == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizarMascota(@PathVariable Long id,
                                                     @RequestBody Mascota nuevaMascota) {
        try {
            Mascota mascotaActualizada = mascotaService.actualizarMascota(id, nuevaMascota);
            return ResponseEntity.ok(mascotaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
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

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerMascotaPorId(@PathVariable Long id) {
    Optional<Mascota> mascota = mascotaService.buscarPorId(id);
    if (mascota.isEmpty()) {
        return ResponseEntity.notFound().build();
    }else{
        return ResponseEntity.ok(mascota.get());
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




}
