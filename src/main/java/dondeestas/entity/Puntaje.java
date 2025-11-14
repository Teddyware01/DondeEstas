package dondeestas.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "puntaje")
public class Puntaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Getter
    @Column(nullable = false)
    private int cantidad;

    @Getter
    @Column(name = "tipo_puntaje", nullable = false)
    private String tipoPuntaje;


    public Puntaje(int cantidad,  String tipoPuntaje) {
        this.cantidad = cantidad;
        this.tipoPuntaje = tipoPuntaje;
    }
    public Puntaje() {}

}
