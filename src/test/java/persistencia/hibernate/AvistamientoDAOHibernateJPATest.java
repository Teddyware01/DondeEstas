package persistencia.hibernate;

import dondeestas.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import persistencia.EMF;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AvistamientoDAOHibernateJPATest {

    private EntityManager em;
    private EntityTransaction tx;
    private AvistamientoDAOHibernateJPA avistamientoDAO;

    @BeforeAll
    void setUpAll() {
        em = EMF.getEMF().createEntityManager();
        avistamientoDAO = new AvistamientoDAOHibernateJPA(em);
    }

    @BeforeEach
    void iniciarTransaccion() {
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    void rollbackTransaccion() {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
    }

    @AfterAll
    void cerrarEntityManager() {
        if (em.isOpen()) em.close();
    }

    // ------------------- Tests existentes -------------------

    @Test
    void testPersistYGet() {
        Usuario usuario = new Usuario("Juan", "Perez", "mail@mail.com",
                "1234", "1134567890", "Centro", "Buenos Aires");
        Ubicacion ubicacion = new Ubicacion(-34.6037, -58.3816, "Centro");
        Estado estado = new Estado("Perdido");

        Mascota mascota = new Mascota(usuario,  "Firulais", "Mediano",
                "Marrón", LocalDate.now(), ubicacion, estado, "Ninguna");

        Avistamiento av = new Avistamiento("foto1.jpg", LocalDateTime.now(), "Comentario ejemplo",
                mascota, usuario, ubicacion);

        em.persist(usuario);
        em.persist(ubicacion);
        em.persist(estado);
        em.persist(mascota);

        Avistamiento guardado = avistamientoDAO.persist(av);
        Avistamiento encontrado = avistamientoDAO.get(guardado.getId());

        assertNotNull(encontrado);
        assertEquals(guardado.getId(), encontrado.getId());
    }

    @Test
    void testFindByMascota() {
        Usuario usuario = new Usuario("Ana", "Lopez", "a@mail.com",
                "1234", "1134567890", "Centro", "Buenos Aires");
        Ubicacion ubicacion = new Ubicacion(-34.60, -58.38, "Centro");
        Estado estado = new Estado("Perdido");

        Mascota mascota = new Mascota(usuario, "Luna", "Pequeño",
                LocalDate.now(), ubicacion, estado, "");

        Avistamiento av = new Avistamiento("foto2.jpg", LocalDateTime.now(), "Avistamiento Luna",
                mascota, usuario, ubicacion);

        em.persist(usuario);
        em.persist(ubicacion);
        em.persist(estado);
        em.persist(mascota);
        em.persist(av);

        List<Avistamiento> lista = avistamientoDAO.findByMascota(mascota.getId());

        assertEquals(1, lista.size());
        assertEquals("Luna", lista.get(0).getMascota().getNombre());
    }

    @Test
    void testFindByUsuario() {
        Usuario usuario = new Usuario("Pedro", "Martinez", "p@mail.com",
                "1234", "1134567890", "Norte", "Buenos Aires");
        Ubicacion ubicacion = new Ubicacion(-34.55, -58.45, "Norte");
        Estado estado = new Estado("Encontrado");

        Mascota mascota = new Mascota(usuario, "Toby", "Grande",
                "Blanco", LocalDate.now(), ubicacion, estado, "");

        Avistamiento av = new Avistamiento("foto3.jpg", LocalDateTime.now(), "Avistamiento Toby",
                mascota, usuario, ubicacion);

        em.persist(usuario);
        em.persist(ubicacion);
        em.persist(estado);
        em.persist(mascota);
        em.persist(av);

        List<Avistamiento> lista = avistamientoDAO.findByUsuario(usuario.getId());
        assertFalse(lista.isEmpty());
    }

    @Test
    void testFindByFecha() {
        LocalDate fecha = LocalDate.of(2024, 10, 20);
        LocalDateTime fechaTime = fecha.atStartOfDay();

        Usuario usuario = new Usuario("Ana", "Lopez", "ana@mail.com",
                "1234", "1134567890", "Oeste", "Buenos Aires");
        Ubicacion ubicacion = new Ubicacion(-34.62, -58.48, "Oeste");
        Estado estado = new Estado("Perdido");

        Mascota mascota = new Mascota(usuario,  "Max", "Mediano",
                "Marrón", fecha, ubicacion, estado, "");

        Avistamiento av = new Avistamiento("foto4.jpg", fechaTime, "Avistamiento Max",
                mascota, usuario, ubicacion);

        em.persist(usuario);
        em.persist(ubicacion);
        em.persist(estado);
        em.persist(mascota);
        em.persist(av);

        List<Avistamiento> lista = avistamientoDAO.findByFecha(fecha);
        assertEquals(1, lista.size());
    }

    @Test
    void testFindByBarrio() {
        Usuario usuario = new Usuario("Lucas", "Diaz", "lucas@mail.com",
                "pass", "1122334455", "Palermo", "Buenos Aires");
        Ubicacion ubicacion = new Ubicacion(-34.57, -58.43, "Palermo");
        Estado estado = new Estado("Perdido");

        Mascota mascota = new Mascota(usuario, "Coco", "Chico",
                "Gris", LocalDate.now(), ubicacion, estado, "");

        Avistamiento av = new Avistamiento("foto5.jpg", LocalDateTime.now(), "Avistamiento Coco",
                mascota, usuario, ubicacion);

        em.persist(usuario);
        em.persist(ubicacion);
        em.persist(estado);
        em.persist(mascota);
        em.persist(av);

        List<Avistamiento> lista = avistamientoDAO.findByBarrio("Palermo");
        assertEquals(1, lista.size());
        assertEquals("Palermo", lista.get(0).getUbicacion().getBarrio());
    }


    @Test
    void testAltaAvistamiento() {
        Usuario usuario = new Usuario("Lucas", "Diaz", "lucas@mail.com",
                "pass", "1122334455", "Centro", "Buenos Aires");
        Ubicacion ubicacion = new Ubicacion(-34.57, -58.43, "Centro");
        Estado estado = new Estado("Perdido");
        Mascota mascota = new Mascota(usuario, "Bobby", "Mediano",
                "Marrón", LocalDate.now(), ubicacion, estado, "");

        em.persist(usuario);
        em.persist(ubicacion);
        em.persist(estado);
        em.persist(mascota);

        Avistamiento av = new Avistamiento("foto6.jpg", LocalDateTime.now(), "Nueva foto",
                mascota, usuario, ubicacion);
        Avistamiento guardado = avistamientoDAO.persist(av);

        assertNotNull(guardado.getId());
        assertEquals("foto6.jpg", guardado.getFoto());
    }

    @Test
    void testBajaAvistamiento() {
        Usuario usuario = new Usuario("Clara", "Lopez", "clara@mail.com",
                "pass", "1122334455", "Norte", "Buenos Aires");
        Ubicacion ubicacion = new Ubicacion(-34.55, -58.42, "Norte");
        Estado estado = new Estado("Encontrado");
        Mascota mascota = new Mascota(usuario, "Rocky", "Grande",
                "Negro", LocalDate.now(), ubicacion, estado, "");

        em.persist(usuario);
        em.persist(ubicacion);
        em.persist(estado);
        em.persist(mascota);

        Avistamiento av = new Avistamiento("foto7.jpg", LocalDateTime.now(), "Comentario",
                mascota, usuario, ubicacion);
        em.persist(av);

        avistamientoDAO.delete(av);

        Avistamiento encontrado = avistamientoDAO.get(av.getId());
        assertNull(encontrado);
    }

    @Test
    void testActualizacionAvistamiento() {
        Usuario usuario = new Usuario("Pedro", "Gomez", "pedro@mail.com",
                "pass", "1122334455", "Sur", "Buenos Aires");
        Ubicacion ubicacion = new Ubicacion(-34.53, -58.44, "Sur");
        Estado estado = new Estado("Perdido");
        Mascota mascota = new Mascota(usuario, "Toby", "Mediano",
                "Marrón", LocalDate.now(), ubicacion, estado, "");

        em.persist(usuario);
        em.persist(ubicacion);
        em.persist(estado);
        em.persist(mascota);

        Avistamiento av = new Avistamiento("foto8.jpg", LocalDateTime.now(), "Comentario inicial",
                mascota, usuario, ubicacion);
        em.persist(av);

        // Actualizar comentario
        av.setComentario("Comentario actualizado");
        avistamientoDAO.update(av);

        Avistamiento encontrado = avistamientoDAO.get(av.getId());
        assertEquals("Comentario actualizado", encontrado.getComentario());
    }
}
