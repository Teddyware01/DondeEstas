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

    @GetMapping // tiene que arrancar listando a las mascotas perdidas
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

        // Buscar el usuario por ID
        Optional<Usuario> usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario.isEmpty()) {
            // Retorna 404 si no existe el usuario
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Asignar el usuario a la mascota
        mascota.setUsuario(usuario.get());

        // Guardar la mascota
        Mascota nueva = mascotaService.registrarMascota(mascota);

        // Retornar la respuesta según si se creó correctamente
        if (nueva == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        }
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
    Optional<Mascota> mascota = mascotaService.buscarPorId(id);
    if (mascota.isEmpty()) {
        return ResponseEntity.notFound().build(); // 404 NOT FOUND
    }else{
        return ResponseEntity.ok(mascota.get()); // 200 OK
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
    public ResponseEntity<List<Mascota>> listarTodas() {
        List<Mascota> mascotas = mascotaService.listarTodas();
        if (mascotas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(mascotas);

    }


}
