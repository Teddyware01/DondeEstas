package dondeestas;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contrasena;

    @Column
    private String telefono;

    @Column
    private String barrio;

    @Column
    private String ciudad;

    @Column
    private RolEnum rol;

    private List<Mascota> misMascotas;
    private List<Mascota> mascotasAjenas;
    private List<Medalla> medallas;
    private List<Avistamiento> avistamientos;
    private List<DetallePuntos> historialPuntos;


    public Usuario(Long id, String nombre, String apellido, String email,
                   String contrasena, String telefono, String barrio, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.barrio = barrio;
        this.ciudad = ciudad;
        this.rol = RolEnum.ESTANDAR;
        this.misMascotas = new ArrayList<>();
        this.mascotasAjenas = new ArrayList<>();
        this.medallas = new ArrayList<>();
        this.avistamientos = new ArrayList<>();
        this.historialPuntos = new ArrayList<>();
    }

    public Usuario() {}

    public void verMascotas(String barrio) {
    }

    public void verRanking() {
    }

    public void editarUsuario() {
    }

    public void crearMascota(Usuario usuario, String nombre, String tamanio, String color, LocalDate fecha, String estado, String fotos, Ubicacion coordenada, String descripcionExtra) {
    }

    public void editarMascota(String nombre, String tamanio, String color, LocalDate fecha, String estado, String fotos, Ubicacion coordenada, String descripcionExtra) {
    }

    public void verMascota() {
    }

    public void eliminarMascota(Integer id) {
    }

    public void reportarAvistamiento(Mascota mascota, Ubicacion coordenadas, String foto, LocalDate fecha, String comentario, Usuario usuario) {

    }

    public void verPerfil() {
    }
    
}
