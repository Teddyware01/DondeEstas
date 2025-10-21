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
    }

    public Usuario() {}

    public void verMascotas(String barrio) {
    }

    public void verRanking() {
    }

    public void editarUsuario() {
    }

    public void verPerfil() {
    }
    
}
