package dondeestas;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_puntos")
public class DetallePuntos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPuntosEnum tipo;

    public DetallePuntos() {}
}
