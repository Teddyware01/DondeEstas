package dondeestas.controller;

import dondeestas.entity.Avistamiento;
import dondeestas.service.AvistamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avistamientos")
public class AvistamientoController {

    @Autowired
    private AvistamientoService avistamientoService;

    @PostMapping
    public ResponseEntity<Avistamiento> registrarAvistamiento(@RequestBody Avistamiento avistamiento) {
        try {
            Avistamiento nuevoAvistamiento = avistamientoService.registrar(avistamiento);
            return new ResponseEntity<>(nuevoAvistamiento, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAvistamiento(@PathVariable Long id) {
        avistamientoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}