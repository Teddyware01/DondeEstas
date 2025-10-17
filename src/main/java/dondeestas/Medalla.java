package dondeestas;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "medalla")
public class  Medalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaObtencion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedallaEnum tipo;

    public Medalla() {}
}
