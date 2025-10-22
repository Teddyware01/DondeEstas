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

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avistamiento> avistamientos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mascota> mascotas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioMedalla> medallas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPuntaje> puntajes;

    @Column(nullable = false)
    private Boolean is_admin;

    public Usuario(String nombre, String apellido, String email,
                   String contrasena, String telefono, String barrio, String ciudad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.barrio = barrio;
        this.ciudad = ciudad;
        this.medallas = new ArrayList<>();
        this.avistamientos = new ArrayList<>();
        this.is_admin = false;
    }

    public Usuario(List<UsuarioMedalla> medallas) {
        this.medallas = medallas;
    }

    public Usuario() {

    }

    public void verMascotas(String barrio) {
    }

    public void verRanking() {
    }

    public void editarUsuario() {
    }

    public void verPerfil() {
    }

    public Long getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return  telefono;
    }
}
