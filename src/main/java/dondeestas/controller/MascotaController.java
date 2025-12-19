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
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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

    @GetMapping("/todos")
    public ResponseEntity<List<MascotaDTO>> listarTodasLasMascotas() {

        List<Mascota> mascotas = mascotaService.listarActivas();

        if (mascotas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<MascotaDTO> resultado = mascotas.stream()
                .map(m -> {

                    List<String> imagenesBase64 = null;

                    if (m.getImagenes() != null && !m.getImagenes().isEmpty()) {
                        imagenesBase64 = m.getImagenes()
                                .stream()
                                .map(img ->
                                        // 👇 prefijo clave
                                        "data:image/jpeg;base64," + img.getImagenBase64()
                                )
                                .toList();
                    }

                    MascotaDTO dto = new MascotaDTO();
                    dto.setId(m.getId());
                    dto.setNombre(m.getNombre());
                    dto.setTamano(m.getTamano());
                    dto.setColor(m.getColor());
                    dto.setEstado(m.getEstado().name());
                    dto.setFecha(m.getFecha());
                    dto.setDescripcionExtra(m.getDescripcionExtra());
                    dto.setTipoAnimal(m.getTipoAnimal().name());
                    dto.setProvincia(m.getProvincia());
                    dto.setDepartamento(m.getDepartamento());
                    dto.setMunicipio(m.getMunicipio());
                    dto.setTelefono(m.getUsuario().getTelefono());
                    dto.setImagenesBase64(imagenesBase64);

                    return dto;
                })
                .toList();

        return ResponseEntity.ok(resultado);
    }


    @GetMapping("/perdidas")
    public ResponseEntity<List<MascotaDTO>> listarMascotasPerdidas() {

        List<Mascota> perdidas = mascotaService.listarMascotasPerdidas();

        if (perdidas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<MascotaDTO> resultado = perdidas.stream()
                .map(m -> {

                    List<String> imagenesBase64 = null;

                    if (m.getImagenes() != null && !m.getImagenes().isEmpty()) {
                        imagenesBase64 = m.getImagenes()
                                .stream()
                                .map(img -> {
                                    // 👇 PREFIJO CLAVE
                                    return "data:image/jpeg;base64," + img.getImagenBase64();
                                })
                                .toList();
                    }

                    MascotaDTO dto = new MascotaDTO();
                    dto.setId(m.getId());
                    dto.setNombre(m.getNombre());
                    dto.setTamano(m.getTamano());
                    dto.setColor(m.getColor());
                    dto.setEstado(m.getEstado().name());
                    dto.setDescripcionExtra(m.getDescripcionExtra());
                    dto.setFecha(m.getFecha());
                    dto.setTipoAnimal(m.getTipoAnimal().name());
                    dto.setProvincia(m.getProvincia());
                    dto.setDepartamento(m.getDepartamento());
                    dto.setMunicipio(m.getMunicipio());
                    dto.setTelefono(m.getUsuario().getTelefono());

                    dto.setImagenesBase64(imagenesBase64);

                    return dto;
                })
                .toList();

        return ResponseEntity.ok(resultado);
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
/*@PostMapping
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

    Ubicacion ubicacion;
    if (dto.getUbicacion() != null && dto.getUbicacion().contains(",")) {
        String[] parts = dto.getUbicacion().split(",");
        try {
            double lat = Double.parseDouble(parts[0].trim());
            double lng = Double.parseDouble(parts[1].trim());
            mascota.setLatitud(lat);
            mascota.setLongitud(lng);
            ubicacion = Ubicacion.obtenerUbicacionPorLatLon(lat, lng);
            mascota.setProvincia(ubicacion.getProvincia());
            mascota.setMunicipio(ubicacion.getMunicipio());
            mascota.setDepartamento(ubicacion.getDepartamento());

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
*/
    @PostMapping
    public ResponseEntity<Mascota> crearMascota(
            @RequestPart("mascota") @Valid MascotaCrearDTO dto,
            @RequestPart(value = "imagenes", required = false) MultipartFile[] imagenes
    ) {
        System.out.println("------------------------------------------------");
        System.out.println("1. DTO Recibido: " + dto);

        // Verificación de Usuario
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(dto.getUsuarioId());
        if (usuarioOpt.isEmpty()) {
            System.err.println("ERROR: Usuario no encontrado con ID: " + dto.getUsuarioId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = usuarioOpt.get();
        Mascota mascota = new Mascota();
        mascota.setNombre(dto.getNombre());
        mascota.setTamano(dto.getTamano());
        mascota.setColor(dto.getColor());
        mascota.setActivo(true);

        mascota.setDescripcionExtra(dto.getDescripcionExtra());
        mascota.setUsuario(usuario);

        // 1. Depuración de FECHA
        if (dto.getFechaPerdida() != null && !dto.getFechaPerdida().isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(dto.getFechaPerdida(), DateTimeFormatter.ISO_DATE);
                mascota.setFecha(fecha);
            } catch (DateTimeParseException e) {
                System.err.println("ERROR: Falló el parseo de fecha. Valor recibido: " + dto.getFechaPerdida());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        // 2. Depuración de ESTADO
        try {
            System.out.println("Intentando parsear estado: " + dto.getEstado());
            mascota.setEstado(EstadoEnum.valueOf(dto.getEstado().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("ERROR: Estado inválido. Valor recibido: " + dto.getEstado());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 3. Depuración de TIPO DE ANIMAL
        try {
            System.out.println("Intentando parsear tipoAnimal: " + dto.getTipoAnimal());
            mascota.setTipoAnimal(
                    TipoAnimalEnum.valueOf(dto.getTipoAnimal().toUpperCase())
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("ERROR: TipoAnimal inválido. Valor recibido: " + dto.getTipoAnimal());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 4. Depuración de UBICACIÓN
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
                System.err.println("ERROR: Falló parseo de ubicación. Valor recibido: " + dto.getUbicacion());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        // 5. Manejo de imágenes (multipart)
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
                System.err.println("ERROR: Falló al procesar las imágenes");
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            mascota.setImagenes(listaImagenes);
        }

        // Guardar mascota
        Mascota nueva = mascotaService.registrarMascota(mascota);
        if (nueva == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizarMascota(
            @PathVariable Long id,
            @RequestBody MascotaActualizarDTO dto) {

        // 1. Buscar mascota existente
        Optional<Mascota> mascotaOpt = mascotaService.buscarPorId(id);
        if (mascotaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Mascota mascota = mascotaOpt.get();

        // 2. Actualizar campos opcionales
        if (dto.getNombre() != null) mascota.setNombre(dto.getNombre());
        if (dto.getTamano() != null) mascota.setTamano(dto.getTamano());
        if (dto.getColor() != null) mascota.setColor(dto.getColor());
        if (dto.getDescripcionExtra() != null) mascota.setDescripcionExtra(dto.getDescripcionExtra());

        // 3. Fecha
        if (dto.getFechaPerdida() != null && !dto.getFechaPerdida().isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(dto.getFechaPerdida(), DateTimeFormatter.ISO_DATE);
                mascota.setFecha(fecha);
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        // 4. Estado
        if (dto.getEstado() != null) {
            try {
                mascota.setEstado(EstadoEnum.valueOf(dto.getEstado().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        // 5. Tipo de animal
        if (dto.getTipoAnimal() != null) {
            try {
                mascota.setTipoAnimal(TipoAnimalEnum.valueOf(dto.getTipoAnimal().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        // 6. Ubicación
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

        // 7. Guardar cambios
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
        if (mascotas.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(mascotas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> obtenerMascotaPorId(@PathVariable Long id) {

        Optional<Mascota> mascotaOpt = mascotaService.buscarPorId(id);

        if (mascotaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Mascota m = mascotaOpt.get();

        List<String> imagenesBase64 = null;

        if (m.getImagenes() != null && !m.getImagenes().isEmpty()) {
            imagenesBase64 = m.getImagenes()
                    .stream()
                    .map(img ->
                            // 👇 MISMO PREFIJO QUE EN /todos y /perdidas
                            "data:image/jpeg;base64," + img.getImagenBase64()
                    )
                    .toList();
        }

        MascotaDTO dto = new MascotaDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setTamano(m.getTamano());
        dto.setColor(m.getColor());
        dto.setEstado(m.getEstado().name());
        dto.setTipoAnimal(m.getTipoAnimal().name());
        dto.setFecha(m.getFecha());
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
        if (perdidas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(perdidas, HttpStatus.OK);
    }
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<Mascota> desactivarMascota(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String authHeader) {

        // Validación del header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        // Buscar la mascota por ID
        Optional<Mascota> mascotaOpt = mascotaService.buscarPorId(id);
        if (mascotaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


        String token = authHeader.substring(7);
        String tokenEsperado = mascotaOpt.get().getUsuario().getId() + "123456"; // mismo patrón que usas para usuarios

        if (!token.equals(tokenEsperado)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Desactivar mascota
        Mascota mascota = mascotaOpt.get();
        mascota.setActivo(false);

        // Guardar cambios
        Mascota mascotaActualizada = mascotaService.actualizarMascota(mascota.getId(), mascota);

        // Devolver la mascota desactivada
        return ResponseEntity.ok(mascotaActualizada);
    }



}
