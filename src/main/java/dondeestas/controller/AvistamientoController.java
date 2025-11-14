package dondeestas.controller;

import dondeestas.entity.Avistamiento;
import dondeestas.entity.Usuario;
import dondeestas.entity.Mascota;
import dondeestas.repository.AvistamientoRepository;
import dondeestas.service.AvistamientoService;
import dondeestas.service.MascotaService;
import dondeestas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/avistamientos")
public class AvistamientoController {

    @Autowired
    private AvistamientoService avistamientoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MascotaService mascotaService;

    @GetMapping("/")
    public ResponseEntity<List<Avistamiento>> getAvistamientos() {
        List<Avistamiento> avistamientos =  avistamientoService.listarTodos();
        if  (avistamientos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return  ResponseEntity.ok(avistamientos);
    }

    @PostMapping("/usuario/{usuarioId}/mascota/{mascotaId}")
    public ResponseEntity<Avistamiento> registrarAvistamiento(
            @Valid @RequestBody Avistamiento avistamiento,
            @PathVariable Long usuarioId,
            @PathVariable Long mascotaId) {

        Optional<Usuario> usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Mascota> mascota = mascotaService.buscarPorId(mascotaId);
        if (mascota.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        avistamiento.setUsuario(usuario.get());
        avistamiento.setMascota(mascota.get());


        try {
            Avistamiento nuevoAvistamiento = avistamientoService.registrar(avistamiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAvistamiento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> getAvistamientoPorId(@PathVariable Long id) {
        return avistamientoService.buscarPorId(id)
                .map(avistamiento -> new ResponseEntity<>(avistamiento, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<Avistamiento>> getAvistamientosPorMascota(@PathVariable Long mascotaId) {
        List<Avistamiento> avistamientos = avistamientoService.buscarPorMascota(mascotaId);
        if (avistamientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(avistamientos, HttpStatus.OK);
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Avistamiento>> getAvistamientosPorUsuario(@PathVariable Long usuarioId) {
        List<Avistamiento> avistamientos = avistamientoService.buscarPorUsuario(usuarioId);
        if (avistamientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(avistamientos, HttpStatus.OK);
    }

}