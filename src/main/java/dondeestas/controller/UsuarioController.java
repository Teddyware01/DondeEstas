package dondeestas.controller;

import dondeestas.entity.Usuario;
import dondeestas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id,
                                                  @RequestHeader("token") String token) {
        String tokenEsperado = id + "123456";

        if (!token.equals(tokenEsperado)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);

        return usuarioOpt
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.registrar(usuario);
        if  (nuevo == null) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }else return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }


    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }

    @PostMapping("/autenticacion")
    public ResponseEntity<Void> autenticarUsuario(
            @RequestHeader("email") String emailUsuario,
            @RequestHeader("contrasena") String contrasena) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(emailUsuario);

        if (usuarioOpt.isPresent() && usuarioOpt.get().getContrasena().equals(contrasena)) {
            String token = usuarioOpt.get().getId() + "123456";
            return ResponseEntity
                    .noContent()
                    .header("token", token)
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id,
                                                     @RequestHeader("token") String token,
                                                     @RequestBody Usuario usuarioActualizado) {
        String tokenEsperado = id + "123456";

        if (!token.equals(tokenEsperado)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setId(id);
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setContrasena(usuarioActualizado.getContrasena());
        usuario.setBarrio(usuarioActualizado.getBarrio());
        usuario.setCiudad(usuarioActualizado.getCiudad());
        usuario.setIsAdmin(usuarioActualizado.getIsAdmin());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        // actualizar otros campos si hay

        Usuario guardado = usuarioService.registrar(usuario);
        return ResponseEntity.ok(guardado);
    }


}
