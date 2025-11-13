package dondeestas.controller;

import dondeestas.entity.Avistamiento;
import dondeestas.service.AvistamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avistamientos") // URI base: /api/avistamientos
public class AvistamientoController {

    @Autowired
    private AvistamientoService avistamientoService;

    @PostMapping
    // @RequestBody: Spring toma el JSON del cuerpo de la petición y lo convierte a un objeto Avistamiento.
    public ResponseEntity<Avistamiento> registrarAvistamiento(@RequestBody Avistamiento avistamiento) {
        try {
            // Delega la lógica de negocio y la persistencia al Servicio
            Avistamiento nuevoAvistamiento = avistamientoService.registrar(avistamiento);
            
            // Retorna la entidad creada con el código 201 CREATED
            return new ResponseEntity<>(nuevoAvistamiento, HttpStatus.CREATED);
        } catch (Exception e) {
            // Manejo genérico de errores de negocio o validación (ej. 400 BAD REQUEST)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> getAvistamientoPorId(@PathVariable Long id) {
        // Delega la búsqueda al Servicio
        return avistamientoService.buscarPorId(id)
                .map(avistamiento -> new ResponseEntity<>(avistamiento, HttpStatus.OK)) // Si existe, retorna 200 OK
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));                      // Si no existe, retorna 404 NOT FOUND
    }
    @GetMapping("/mascota/{mascotaId}")
    public List<Avistamiento> getAvistamientosPorMascota(@PathVariable Long mascotaId) {
        // Delega la consulta compleja al Servicio
        return avistamientoService.buscarPorMascota(mascotaId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAvistamiento(@PathVariable Long id) {
        // Delega la eliminación (incluida la ruptura de bidireccionalidad) al Servicio
        avistamientoService.eliminar(id);

        // Retorna 204 NO CONTENT (Éxito sin cuerpo en la respuesta)
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}