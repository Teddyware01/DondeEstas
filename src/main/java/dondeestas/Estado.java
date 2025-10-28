package dondeestas;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreEstado;

    @OneToMany(mappedBy = "estado")
    private List<Mascota> mascotas;

    public Estado() {}
    public Estado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
        this.mascotas = new ArrayList<>();
    }

    public void addMascota(Mascota mascota) {
        mascotas.add(mascota);
    }

    public String getNombreEstado() {
        return nombreEstado;
    }
}
