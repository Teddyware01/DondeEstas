package dondeestas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private double latitud;

    @Getter
    @Column(nullable = false)
    private double longitud;

    @Getter
    @Column(nullable = false)
    private String barrio;

    @Getter
    @Setter
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

    public void addMascota(Mascota mascota) {
        mascotas.add(mascota);
    }
    public void addAvistamiento(Avistamiento avistamiento) {
        avistamientos.add(avistamiento);
    }
}