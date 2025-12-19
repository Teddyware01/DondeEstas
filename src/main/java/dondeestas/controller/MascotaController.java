package dondeestas.controller;

import dondeestas.auxClass.EstadoEnum;
import dondeestas.auxClass.TipoAnimalEnum;
import dondeestas.auxClass.Ubicacion;
import dondeestas.dto.MascotaActualizarDTO;
import dondeestas.dto.MascotaCrearDTO;
import dondeestas.dto.MascotaDTO;
import dondeestas.entity.Mascota;
import dondeestas.entity.MascotaImagen;
import dondeestas.entity.Usuario;
import dondeestas.service.MascotaService;
import dondeestas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;
    @Autowired
    private UsuarioService usuarioService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    @GetMapping("/todos")
    public ResponseEntity<List<MascotaDTO>> listarTodasLasMascotas() {
        List<Mascota> mascotas = mascotaService.listarActivas();
        if (mascotas.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<MascotaDTO> resultado = mascotas.stream().map(m -> {
            List<String> imagenesBase64 = null;
            if (m.getImagenes() != null && !m.getImagenes().isEmpty()) {
                imagenesBase64 = m.getImagenes()
                        .stream()
                        .map(img -> "data:image/jpeg;base64," + img.getImagenBase64())
                        .toList();
            }

            MascotaDTO dto = new MascotaDTO();
            dto.setId(m.getId());
            dto.setNombre(m.getNombre());
            dto.setTamano(m.getTamano());
            dto.setColor(m.getColor());
            dto.setEstado(m.getEstado().name());
            dto.setFechaPerdida(m.getFechaPerdida() != null ? m.getFechaPerdida().format(formatter) : null);
            dto.setDescripcionExtra(m.getDescripcionExtra());
            dto.setTipoAnimal(m.getTipoAnimal().name());
            dto.setProvincia(m.getProvincia());
            dto.setDepartamento(m.getDepartamento());
            dto.setMunicipio(m.getMunicipio());
            dto.setTelefono(m.getUsuario().getTelefono());
            dto.setImagenesBase64(imagenesBase64);

            return dto;
        }).toList();

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/perdidas")
    public ResponseEntity<List<MascotaDTO>> listarMascotasPerdidas() {
        List<Mascota> perdidas = mascotaService.listarMascotasPerdidas();
        if (perdidas.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<MascotaDTO> resultado = perdidas.stream().map(m -> {
            List<String> imagenesBase64 = null;
            if (m.getImagenes() != null && !m.getImagenes().isEmpty()) {
                imagenesBase64 = m.getImagenes()
                        .stream()
                        .map(img -> "data:image/jpeg;base64," + img.getImagenBase64())
                        .toList();
            }

            MascotaDTO dto = new MascotaDTO();
            dto.setId(m.getId());
            dto.setNombre(m.getNombre());
            dto.setTamano(m.getTamano());
            dto.setColor(m.getColor());
            dto.setEstado(m.getEstado().name());
            dto.setDescripcionExtra(m.getDescripcionExtra());
            dto.setFechaPerdida(m.getFechaPerdida() != null ? m.getFechaPerdida().format(formatter) : null);
            dto.setTipoAnimal(m.getTipoAnimal().name());
            dto.setProvincia(m.getProvincia());
            dto.setDepartamento(m.getDepartamento());
            dto.setMunicipio(m.getMunicipio());
            dto.setTelefono(m.getUsuario().getTelefono());
            dto.setImagenesBase64(imagenesBase64);

            return dto;
        }).toList();

        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<Mascota> crearMascota(
            @RequestPart("mascota") @Valid MascotaCrearDTO dto,
            @RequestPart(value = "imagenes", required = false) MultipartFile[] imagenes
    ) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(dto.getUsuarioId());
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Usuario usuario = usuarioOpt.get();
        Mascota mascota = new Mascota();
        mascota.setNombre(dto.getNombre());
        mascota.setTamano(dto.getTamano());
        mascota.setColor(dto.getColor());
        mascota.setActivo(true);
        mascota.setDescripcionExtra(dto.getDescripcionExtra());
        mascota.setUsuario(usuario);

        // Parse fechaPerdida de String a LocalDate
        if (dto.getFechaPerdida() != null && !dto.getFechaPerdida().isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(dto.getFechaPerdida(), formatter);
                mascota.setFechaPerdida(fecha);
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        try {
            mascota.setEstado(EstadoEnum.valueOf(dto.getEstado().toUpperCase()));
            mascota.setTipoAnimal(TipoAnimalEnum.valueOf(dto.getTipoAnimal().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (dto.getUbicacion() != null && dto.getUbicacion().contains(",")) {
            String[] parts = dto.getUbicacion().split(",");
            try {
                double lat = Double.parseDouble(parts[0].trim());
                double lng = Double.parseDouble(parts[1].trim());
                mascota.setLatitud(lat);
                mascota.setLongitud(lng);
                Ubicacion ubicacion = Ubicacion.obtenerUbicacionPorLatLon(lat, lng);
                mascota.setProvincia(ubicacion.getProvincia());
                mascota.setMunicipio(ubicacion.getMunicipio());
                mascota.setDepartamento(ubicacion.getDepartamento());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        if (imagenes != null && imagenes.length > 0) {
            List<MascotaImagen> listaImagenes = new ArrayList<>();
            try {
                for (MultipartFile file : imagenes) {
                    if (!file.isEmpty()) {
                        String base64 = Base64.getEncoder().encodeToString(file.getBytes());
                        listaImagenes.add(new MascotaImagen(mascota, base64));
                    }
                }
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            mascota.setImagenes(listaImagenes);
        }

        Mascota nueva = mascotaService.registrarMascota(mascota);
        if (nueva == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizarMascota(
            @PathVariable Long id,
            @RequestBody MascotaActualizarDTO dto) {

        Optional<Mascota> mascotaOpt = mascotaService.buscarPorId(id);
        if (mascotaOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Mascota mascota = mascotaOpt.get();

        if (dto.getNombre() != null) mascota.setNombre(dto.getNombre());
        if (dto.getTamano() != null) mascota.setTamano(dto.getTamano());
        if (dto.getColor() != null) mascota.setColor(dto.getColor());
        if (dto.getDescripcionExtra() != null) mascota.setDescripcionExtra(dto.getDescripcionExtra());

        // Parse fechaPerdida
        if (dto.getFechaPerdida() != null && !dto.getFechaPerdida().isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(dto.getFechaPerdida(), formatter);
                mascota.setFechaPerdida(fecha);
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        if (dto.getEstado() != null) {
            try { mascota.setEstado(EstadoEnum.valueOf(dto.getEstado().toUpperCase())); }
            catch (IllegalArgumentException e) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); }
        }

        if (dto.getTipoAnimal() != null) {
            try { mascota.setTipoAnimal(TipoAnimalEnum.valueOf(dto.getTipoAnimal().toUpperCase())); }
            catch (IllegalArgumentException e) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); }
        }

        if (dto.getUbicacion() != null && dto.getUbicacion().contains(",")) {
            String[] parts = dto.getUbicacion().split(",");
            try {
                double lat = Double.parseDouble(parts[0].trim());
                double lng = Double.parseDouble(parts[1].trim());
                mascota.setLatitud(lat);
                mascota.setLongitud(lng);
                Ubicacion ubicacion = Ubicacion.obtenerUbicacionPorLatLon(lat, lng);
                mascota.setProvincia(ubicacion.getProvincia());
                mascota.setMunicipio(ubicacion.getMunicipio());
                mascota.setDepartamento(ubicacion.getDepartamento());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        Mascota mascotaActualizada = mascotaService.actualizarMascota(id, mascota);
        return ResponseEntity.ok(mascotaActualizada);
    }

    @DeleteMapping("/{id}")
    public void eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Mascota>> listarMascotasPorUsuario(@PathVariable Long idUsuario) {
        List<Mascota> mascotas = mascotaService.buscarPorUsuario(idUsuario);
        if (mascotas.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(mascotas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> obtenerMascotaPorId(@PathVariable Long id) {
        Optional<Mascota> mascotaOpt = mascotaService.buscarPorId(id);
        if (mascotaOpt.isEmpty()) return ResponseEntity.notFound().build();

        Mascota m = mascotaOpt.get();

        List<String> imagenesBase64 = null;
        if (m.getImagenes() != null && !m.getImagenes().isEmpty()) {
            imagenesBase64 = m.getImagenes()
                    .stream()
                    .map(img -> "data:image/jpeg;base64," + img.getImagenBase64())
                    .toList();
        }

        MascotaDTO dto = new MascotaDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setTamano(m.getTamano());
        dto.setColor(m.getColor());
        dto.setEstado(m.getEstado().name());
        dto.setTipoAnimal(m.getTipoAnimal().name());
        dto.setFechaPerdida(m.getFechaPerdida() != null ? m.getFechaPerdida().format(formatter) : null);
        dto.setDescripcionExtra(m.getDescripcionExtra());
        dto.setProvincia(m.getProvincia());
        dto.setDepartamento(m.getDepartamento());
        dto.setMunicipio(m.getMunicipio());
        dto.setImagenesBase64(imagenesBase64);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/encontradas")
    public ResponseEntity<List<Mascota>> listarMascotasEncontradas() {
        List<Mascota> perdidas = mascotaService.listarMascotasEncontradas();
        if (perdidas.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(perdidas, HttpStatus.OK);
    }

    @PutMapping("/desactivar/{id}")
    public ResponseEntity<Mascota> desactivarMascota(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Mascota> mascotaOpt = mascotaService.buscarPorId(id);
        if (mascotaOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        String token = authHeader.substring(7);
        String tokenEsperado = mascotaOpt.get().getUsuario().getId() + "123456";
        if (!token.equals(tokenEsperado)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Mascota mascota = mascotaOpt.get();
        mascota.setActivo(false);

        Mascota mascotaActualizada = mascotaService.actualizarMascota(mascota.getId(), mascota);
        return ResponseEntity.ok(mascotaActualizada);
    }

}
