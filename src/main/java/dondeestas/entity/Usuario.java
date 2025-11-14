package dondeestas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {



    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private String nombre;

    @Getter
    @Setter
    @Column(nullable = false)
    private String apellido;

    @Getter
    @Setter
    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @NotNull
    @Getter
    @Setter
    private String contrasena;

    @Getter
    @Setter
    @NotNull
    @Column
    private String telefono;

    @Column
    @Getter
    @Setter
    private String barrio;

    @Column
    @Getter
    @Setter
    private String ciudad;

    @Getter
    @JsonIgnore
    @Setter
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avistamiento> avistamientos;

    @Getter
    @JsonIgnore
    @Setter
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mascota> mascotas;

    @Getter
    @JsonIgnore
    @Setter
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioMedalla> medallas;

    @Getter
    @JsonIgnore
    @Setter
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPuntaje> puntajes;

    @Getter
    @Setter
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

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
        this.mascotas = new ArrayList<>();
        this.isAdmin = false;
        this.puntajes = new ArrayList<>();

    }

    public Usuario() {

    }


}



