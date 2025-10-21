package dondeestas;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "puntaje")
public class Puntaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private String tipoPuntaje;

    @OneToMany(mappedBy = "puntaje", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPuntaje> usuarios;


    public Puntaje() {}
}
