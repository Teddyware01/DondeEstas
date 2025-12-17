package dondeestas.controller;

import dondeestas.auxClass.EstadoEnum;
import dondeestas.dto.MascotaCrearDTO;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

/*
    @PostMapping
    public ResponseEntity<Mascota> crearMascota(@Valid @RequestBody MascotaCrearDTO dto) {
        System.out.println("Se recibio mascota: "+dto.toString());
        Optional<Usuario> usuario = usuarioService.buscarPorId(dto.getUsuarioId());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Mascota mascota = new Mascota();
        mascota.setNombre(dto.getNombre());
        mascota.setTamano(dto.getTamano());
        mascota.setColor(dto.getColor());

        if (dto.getFechaPerdida() != null && !dto.getFechaPerdida().isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(dto.getFechaPerdida(), DateTimeFormatter.ISO_DATE);
                mascota.setFecha(fecha);
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Fecha inválida
            }
        }


        try {
            mascota.setEstado(EstadoEnum.valueOf(dto.getEstado().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // valor inválido para el enum
        }
        mascota.setImagenBase64(dto.getFoto()); // base64
        mascota.setUsuario(usuario.get());


        if (dto.getUbicacion() != null && dto.getUbicacion().contains(",")) {
            String[] parts = dto.getUbicacion().split(",");
            try {
                double lat = Double.parseDouble(parts[0].trim());
                double lng = Double.parseDouble(parts[1].trim());
                mascota.setLatitud(lat);
                mascota.setLongitud(lng);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null); // Ubicación inválida
            }
        }


        Mascota nueva = mascotaService.registrarMascota(mascota);

        if (nueva == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        }
    }

  */
@PostMapping
public ResponseEntity<Mascota> crearMascota(@Valid @RequestBody MascotaCrearDTO dto) {
    System.out.println("------------------------------------------------");
    System.out.println("1. DTO Recibido: " + dto);

    // Verificación de Usuario
    Optional<Usuario> usuario = usuarioService.buscarPorId(dto.getUsuarioId());
    if (usuario.isEmpty()) {
        System.err.println("ERROR: Usuario no encontrado con ID: " + dto.getUsuarioId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    Mascota mascota = new Mascota();
    mascota.setNombre(dto.getNombre());
    mascota.setTamano(dto.getTamano());
    mascota.setColor(dto.getColor());

    // 1. Depuración de FECHA
    if (dto.getFechaPerdida() != null && !dto.getFechaPerdida().isEmpty()) {
        try {
            LocalDate fecha = LocalDate.parse(dto.getFechaPerdida(), DateTimeFormatter.ISO_DATE);
            mascota.setFecha(fecha);
        } catch (DateTimeParseException e) {
            System.err.println("ERROR: Falló el parseo de fecha. Valor recibido: " + dto.getFechaPerdida());
            e.printStackTrace(); // <--- IMPORTANTE VER ESTO
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 2. Depuración de ESTADO
    try {
        System.out.println("Intentando parsear estado: " + dto.getEstado());
        mascota.setEstado(EstadoEnum.valueOf(dto.getEstado().toUpperCase()));
    } catch (IllegalArgumentException | NullPointerException e) {
        System.err.println("ERROR: Estado inválido. Valor recibido: " + dto.getEstado());
        e.printStackTrace(); // <--- IMPORTANTE VER ESTO
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    mascota.setImagenBase64(dto.getFoto());
    mascota.setUsuario(usuario.get());

    // 3. Depuración de UBICACIÓN
    if (dto.getUbicacion() != null && dto.getUbicacion().contains(",")) {
        String[] parts = dto.getUbicacion().split(",");
        try {
            double lat = Double.parseDouble(parts[0].trim());
            double lng = Double.parseDouble(parts[1].trim());
            mascota.setLatitud(lat);
            mascota.setLongitud(lng);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Falló parseo de ubicación. Valor recibido: " + dto.getUbicacion());
            e.printStackTrace(); // <--- IMPORTANTE VER ESTO
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

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
