package dondeestas;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "puntaje")
public class Puntaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Es la cantidad de puntos que "paga" cada tipo.
    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private String tipoPuntaje;

    @OneToMany(mappedBy = "puntaje", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPuntaje> usuarios;


    public Puntaje(int cantidad,  String tipoPuntaje) {
        this.cantidad = cantidad;
        this.tipoPuntaje = tipoPuntaje;
    }
    public Puntaje() {}

    public String getTipoPuntaje() {
        return  tipoPuntaje;
    }

    public int getCantidad() {
        return cantidad;
    }
}
