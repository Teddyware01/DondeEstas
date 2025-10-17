package org.example.dondeestas;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuarioAutor;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String tamano;

    @Column
    private String color;

    @Column
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado;

    @ElementCollection
    private List<String> fotos;

    @OneToOne
    private Ubicacion ubicacion;

    @Column(length = 500)
    private String descripcionExtra;

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public Mascota() {}
}
