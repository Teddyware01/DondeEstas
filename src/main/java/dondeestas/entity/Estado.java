package dondeestas.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "nombre_estado", nullable = false)
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

}
