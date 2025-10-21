package dondeestas;

import jakarta.persistence.*;

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

}