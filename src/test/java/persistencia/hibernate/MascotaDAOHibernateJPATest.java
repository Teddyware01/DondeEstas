package persistencia.hibernate;

import dondeestas.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import persistencia.EMF;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MascotaDAOHibernateJPATest {

    private static EntityManager em;
    private static MascotaDAOHibernateJPA mascotaDAO;

    @BeforeAll
    static void inicializar() {
        em = EMF.getEMF().createEntityManager();
        mascotaDAO = new MascotaDAOHibernateJPA(em);
    }

    @BeforeEach
    void abrirTransaccion() {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
    }

    @AfterEach
    void limpiarDatos() {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }

        em.createQuery("DELETE FROM Avistamiento").executeUpdate();
        em.createQuery("DELETE FROM Mascota").executeUpdate();
        em.createQuery("DELETE FROM Estado").executeUpdate();
        em.createQuery("DELETE FROM Ubicacion").executeUpdate();
        em.createQuery("DELETE FROM Usuario").executeUpdate();

        tx.commit();
    }

    // ------------------- Tests existentes -------------------

    @Test
    void testPersistYGet() {
        Usuario usuario = new Usuario("Juan", "Perez", "juan@mail.com",
                "1234", "123456789", "Centro", "Ciudad");
        em.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        em.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.60, -58.38, "Centro");
        em.persist(ubicacion);

        Mascota mascota = new Mascota(
                usuario,
                "Firulais",
                "Mediano",
                "Marron",
                LocalDate.now(),
                ubicacion,
                estado,
                "Mascota amistosa"
        );

        Mascota guardada = mascotaDAO.persist(mascota);
        Mascota encontrada = mascotaDAO.get(guardada.getId());

        assertNotNull(encontrada);
        assertEquals("Firulais", encontrada.getNombre());
    }

    @Test
    void testFindByEstado() {
        Usuario usuario = new Usuario("Ana", "Lopez", "ana@mail.com",
                "1234", "123456789", "Centro", "Ciudad");
        em.persist(usuario);

        Estado perdido = new Estado("PERDIDO");
        Estado encontrado = new Estado("ENCONTRADO");
        em.persist(perdido);
        em.persist(encontrado);

        Ubicacion ubicacion = new Ubicacion(-34.60, -58.38, "Centro");
        em.persist(ubicacion);

        Mascota m1 = new Mascota(usuario, "Luna", "Pequeño",
                "Blanco", LocalDate.now(), ubicacion, perdido, "...");
        Mascota m2 = new Mascota(usuario, "Max", "Grande",
                "Negro", LocalDate.now(), ubicacion, encontrado, "...");
        Mascota m3 = new Mascota(usuario, "Tucan", "Mediano",
                "Negro", LocalDate.now(), ubicacion, perdido, "...");

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);

        List<Mascota> perdidas = mascotaDAO.findByEstado(perdido);
        List<Mascota> encontradas = mascotaDAO.findByEstado(encontrado);

        assertEquals(2, perdidas.size());
        assertEquals(1, encontradas.size());
        assertEquals("Luna", perdidas.get(0).getNombre());
        assertEquals("Tucan", perdidas.get(1).getNombre());
        assertEquals("Max", encontradas.get(0).getNombre());
    }

    @Test
    void testFindByUsuario() {
        Usuario usuario = new Usuario("Pedro", "Diaz", "p@mail.com",
                "1234", "123456789", "Norte", "Ciudad");
        em.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        em.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.55, -58.40, "Norte");
        em.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Toby", "Mediano",
                "Marron", LocalDate.now(), ubicacion, estado, "...");
        em.persist(mascota);

        List<Mascota> lista = mascotaDAO.findByUsuario(usuario.getId());

        assertEquals(1, lista.size());
        assertEquals("Toby", lista.get(0).getNombre());
    }

    @Test
    void testFindByBarrio() {
        Usuario usuario = new Usuario("Lucia", "Gomez", "l@mail.com",
                "1234", "123456789", "Palermo", "Ciudad");
        em.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        em.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.58, -58.42, "Palermo");
        em.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Milo", "Pequeño",
                "Negro", LocalDate.now(), ubicacion, estado, "...");
        em.persist(mascota);

        List<Mascota> lista = mascotaDAO.findByBarrio("Palermo");

        assertEquals(1, lista.size());
        assertEquals("Palermo", lista.get(0).getUbicacion().getBarrio());
    }

    @Test
    void testSearchByNombreExacto() {
        Usuario usuario = new Usuario("Sofia", "Martinez", "sofia@mail.com",
                "1234", "123456789", "Sur", "Ciudad");
        em.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        em.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.60, -58.45, "Sur");
        em.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Coco", "Pequeño",
                "Blanco", LocalDate.now(), ubicacion, estado, "...");
        em.persist(mascota);

        List<Mascota> lista = mascotaDAO.searchByNombreExacto("Coco");

        assertEquals(1, lista.size());
        assertEquals("Coco", lista.get(0).getNombre());
    }

    @Test
    void testSearchByNombreContains() {
        Usuario usuario = new Usuario("Mario", "Rios", "mario@mail.com",
                "1234", "123456789", "Centro", "Ciudad");
        em.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        em.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.60, -58.38, "Centro");
        em.persist(ubicacion);

        Mascota m1 = new Mascota(usuario, "Luna", "Pequeño",
                "Blanco", LocalDate.now(), ubicacion, estado, "...");
        Mascota m2 = new Mascota(usuario, "Lucho", "Mediano",
                "Negro", LocalDate.now(), ubicacion, estado, "...");

        em.persist(m1);
        em.persist(m2);

        List<Mascota> lista = mascotaDAO.searchByNombreContains("Lu");

        assertEquals(2, lista.size());
    }

    // ------------------- Nuevos tests Alta, Baja, Actualización -------------------

    @Test
    void testAltaMascota() {
        Usuario usuario = new Usuario("Lucas", "Diaz", "lucas@mail.com",
                "1234", "5555555", "Centro", "Ciudad");
        em.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        em.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.61, -58.39, "Centro");
        em.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Bobby", "Mediano",
                "Marron", LocalDate.now(), ubicacion, estado, "Mascota amigable");

        Mascota guardada = mascotaDAO.persist(mascota);

        assertNotNull(guardada.getId());
        assertEquals("Bobby", guardada.getNombre());
    }

    @Test
    void testBajaMascota() {
        Usuario usuario = new Usuario("Clara", "Lopez", "clara@mail.com",
                "1234", "6666666", "Centro", "Ciudad");
        em.persist(usuario);

        Estado estado = new Estado("ENCONTRADO");
        em.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.62, -58.37, "Norte");
        em.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Rocky", "Grande",
                "Negro", LocalDate.now(), ubicacion, estado, "Mascota tranquila");
        em.persist(mascota);

        mascotaDAO.delete(mascota);

        Mascota encontrada = mascotaDAO.get(mascota.getId());
        assertNull(encontrada);
    }

    @Test
    void testActualizacionMascota() {
        Usuario usuario = new Usuario("Pedro", "Gomez", "pedro@mail.com",
                "1234", "7777777", "Sur", "Ciudad");
        em.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        em.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.63, -58.36, "Sur");
        em.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Toby", "Mediano",
                "Marron", LocalDate.now(), ubicacion, estado, "Mascota activa");
        em.persist(mascota);

        // Actualizar nombre y tamaño
        mascota.setNombre("Toby Updated");
        mascota.setTamano("Grande");
        mascotaDAO.update(mascota);

        Mascota encontrada = mascotaDAO.get(mascota.getId());
        assertEquals("Toby Updated", encontrada.getNombre());
        assertEquals("Grande", encontrada.getTamano());
    }
}
