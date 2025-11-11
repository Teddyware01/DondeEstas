package dondeestas.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitud;

    @Column(nullable = false)
    private double longitud;

    @Column(nullable = false)
    private String barrio;

    @OneToMany(mappedBy = "ubicacion")
    private List<Avistamiento> avistamientos;

    @OneToMany(mappedBy = "ubicacion")
    private List<Mascota> mascotas;

    public Ubicacion(double latitud, double longitud, String barrio){
        this.latitud = latitud;
        this.longitud = longitud;
        this.barrio = barrio;
        this.avistamientos = new ArrayList<>();
        this.mascotas = new ArrayList<>();
    }

    public Ubicacion() {

    }

    public String getBarrio() {
        return barrio;
    }

    public Long getId() {
        return id;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void addMascota(Mascota mascota) {
        mascotas.add(mascota);
    }
    public void addAvistamiento(Avistamiento avistamiento) {
        avistamientos.add(avistamiento);
    }
}