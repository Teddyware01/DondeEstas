package dondeestas.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "medalla")
public class  Medalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name ="nombre_medalla", nullable = false)
    private String nombreMedalla;


    public Medalla(String  nombreMedalla) {
        this.nombreMedalla = nombreMedalla;
    }
    public Medalla() {}

}
