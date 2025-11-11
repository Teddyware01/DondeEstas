package dondeestas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medalla")
public class  Medalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreMedalla;


    public Medalla(String  nombreMedalla) {
        this.nombreMedalla = nombreMedalla;
    }
    public Medalla() {}

    public String getNombreMedalla() {
        return nombreMedalla;
    }
}
