package dondeestas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import persistencia.DAO.FactoryDAO;
import persistencia.DAO.UsuarioDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
public class Usuario {



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
    @Getter
    @Setter
    private String contrasena;

    @Getter
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
        this.mascotas = new ArrayList<>();
        this.is_admin = false;
        this.puntajes = new ArrayList<>();

    }

    public Usuario() {

    }


    private void agregarMascota(Mascota mascota){
        this.mascotas.add(mascota);
        mascota.setUsuario(this);
    }

    public static Usuario crearYGuardar(String nombre, String apellido, String email,
                                        String contrasena, String telefono, String barrio, String ciudad){
        Usuario usu = new Usuario( nombre,  apellido,  email, contrasena, telefono,  barrio,  ciudad);
        UsuarioDAO usuarioDAO = FactoryDAO.getUsuarioDAO();
        usuarioDAO.persist(usu);
        return usu;
    }

    public void agregarAvistamiento(Avistamiento avistamiento) {
        this.avistamientos.add(avistamiento);
    }




    //recibe objetos instanciados, pero la "BACK REFERENCE" en ellos se actualiza aca.
    public Mascota crearMascota(String nombre, String tamano, String color, LocalDate fecha, Ubicacion ubicacion, Estado estado, String descripcionExtra ){
        Mascota mascota = new Mascota(this, nombre, tamano, color, fecha, ubicacion,estado, descripcionExtra);
        FactoryDAO.getMascotaDAO().persist(mascota);
        estado.addMascota(mascota);
        FactoryDAO.getEstadoDAO().update(estado);
        ubicacion.addMascota(mascota);
        FactoryDAO.getUbicacionDAO().update(ubicacion);
        this.agregarMascota(mascota);
        return mascota;
    }


    public void agregarPuntaje(Puntaje puntaje){
        UsuarioPuntaje usuPun = new UsuarioPuntaje(this, puntaje, LocalDate.now());
        FactoryDAO.getUsuarioPuntajeDAO().persist(usuPun);
        this.puntajes.add(usuPun);
        FactoryDAO.getUsuarioDAO().update(this);
    }

    public void agregarMedalla(Medalla medalla){
        UsuarioMedalla usuMed = new UsuarioMedalla(this, medalla);
        FactoryDAO.getUsuarioMedallaDAO().persist(usuMed);
        this.medallas.add(usuMed);
        FactoryDAO.getUsuarioDAO().update(this);
    }

    public List<Mascota> verMascotas() {
        return  mascotas;
    }




    public  void editarUsuario(String nombre, String apellido, String email,
                                        String contrasena, String telefono, String barrio, String ciudad){
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.barrio = barrio;
        this.ciudad = ciudad;

        UsuarioDAO usuarioDAO = FactoryDAO.getUsuarioDAO();
        usuarioDAO.update(this);
    }

    public void borrarUsuario(){
        FactoryDAO.getUsuarioDAO().delete(this);
    }

    public static Usuario getUsuario(Long id){
        return FactoryDAO.getUsuarioDAO().get(id);
    }

    public List<UsuarioMedalla> verDetalleMedallas() {
        return  medallas;
    }

    public List<UsuarioPuntaje> verDetallePuntajes() {
        return  puntajes;
    }

    public List<Medalla> verMedallas() {
        return medallas.stream().map(um->um.getMedalla()).collect(Collectors.toList());
    }

    public Integer getTotalPuntos() {
        return FactoryDAO.getUsuarioPuntajeDAO().getTotalPuntosByUsuario(getId());
    }

}



