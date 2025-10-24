package persistencia.hibernate;

import dondeestas.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import persistencia.DAO.FactoryDAO;
import persistencia.EMF;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MascotaDAOHibernateJPATest {

    private static MascotaDAOHibernateJPA mascotaDAO;
    private static UsuarioDAOHibernateJPA usuarioDAO;
    private static EstadoDAOHibernateJPA estadoDAO;

    private static UbicacionDAOHibernateJPA ubicacionDAO;
    private static EntityManager em = EMF.getEMF().createEntityManager();

    @BeforeAll
    static void inicializar() {
        mascotaDAO = (MascotaDAOHibernateJPA) FactoryDAO.getMascotaDAO(em);
        usuarioDAO = (UsuarioDAOHibernateJPA) FactoryDAO.getUsuarioDAO(em);
        estadoDAO = (EstadoDAOHibernateJPA) FactoryDAO.getEstadoDAO(em);
        ubicacionDAO = (UbicacionDAOHibernateJPA) FactoryDAO.getUbicacionDAO(em);
    }

    @AfterEach
    void limpiarDatos() {
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.createQuery("DELETE FROM Avistamiento").executeUpdate();
                em.createQuery("DELETE FROM Mascota").executeUpdate();
                em.createQuery("DELETE FROM Usuario").executeUpdate();
                em.createQuery("DELETE FROM Estado").executeUpdate();
                em.createQuery("DELETE FROM Ubicacion").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw new RuntimeException("Error limpiando datos de prueba: ", e);
            }
        }
    }

    @Test
    void testAltaMascota() {
        Usuario usuario = new Usuario("Juan", "Perez", "juan@mail.com",
                "1234", "123456789", "Centro", "Ciudad");
        usuarioDAO.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        estadoDAO.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.60, -58.38, "Centro");
        ubicacionDAO.persist(ubicacion);

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
        usuarioDAO.persist(usuario);

        Estado perdido = new Estado("PERDIDO");
        Estado encontrado = new Estado("ENCONTRADO");
        estadoDAO.persist(perdido);
        estadoDAO.persist(encontrado);

        Ubicacion ubicacion = new Ubicacion(-34.60, -58.38, "Centro");
        ubicacionDAO.persist(ubicacion);

        Mascota m1 = new Mascota(usuario, "Luna", "Pequeño",
                "Blanco", LocalDate.now(), ubicacion, perdido, "...");
        Mascota m2 = new Mascota(usuario, "Max", "Grande",
                "Negro", LocalDate.now(), ubicacion, encontrado, "...");
        Mascota m3 = new Mascota(usuario, "Tucan", "Mediano",
                "Negro", LocalDate.now(), ubicacion, perdido, "...");

        mascotaDAO.persist(m1);
        mascotaDAO.persist(m2);
        mascotaDAO.persist(m3);

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
        usuarioDAO.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        estadoDAO.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.55, -58.40, "Norte");
        ubicacionDAO.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Toby", "Mediano",
                "Marron", LocalDate.now(), ubicacion, estado, "...");
        mascotaDAO.persist(mascota);

        List<Mascota> lista = mascotaDAO.findByUsuario(usuario.getId());

        assertEquals(1, lista.size());
        assertEquals("Toby", lista.get(0).getNombre());
    }

    @Test
    void testFindByBarrio() {
        Usuario usuario = new Usuario("Lucia", "Gomez", "l@mail.com",
                "1234", "123456789", "Palermo", "Ciudad");
        usuarioDAO.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        estadoDAO.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.58, -58.42, "Palermo");
        ubicacionDAO.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Milo", "Pequeño",
                "Negro", LocalDate.now(), ubicacion, estado, "...");
        mascotaDAO.persist(mascota);

        List<Mascota> lista = mascotaDAO.findByBarrio("Palermo");

        assertEquals(1, lista.size());
        assertEquals("Palermo", lista.get(0).getUbicacion().getBarrio());
    }

    @Test
    void testSearchByNombreExacto() {
        Usuario usuario = new Usuario("Sofia", "Martinez", "sofia@mail.com",
                "1234", "123456789", "Sur", "Ciudad");
        usuarioDAO.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        estadoDAO.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.60, -58.45, "Sur");
        ubicacionDAO.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Coco", "Pequeño",
                "Blanco", LocalDate.now(), ubicacion, estado, "...");
        mascotaDAO.persist(mascota);

        List<Mascota> lista = mascotaDAO.searchByNombreExacto("Coco");

        assertEquals(1, lista.size());
        assertEquals("Coco", lista.get(0).getNombre());
    }

    @Test
    void testSearchByNombreContains() {
        Usuario usuario = new Usuario("Mario", "Rios", "mario@mail.com",
                "1234", "123456789", "Centro", "Ciudad");
        usuarioDAO.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        estadoDAO.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.60, -58.38, "Centro");
        ubicacionDAO.persist(ubicacion);

        Mascota m1 = new Mascota(usuario, "Luna", "Pequeño",
                "Blanco", LocalDate.now(), ubicacion, estado, "...");
        Mascota m2 = new Mascota(usuario, "Lucho", "Mediano",
                "Negro", LocalDate.now(), ubicacion, estado, "...");

        mascotaDAO.persist(m1);
        mascotaDAO.persist(m2);

        List<Mascota> lista = mascotaDAO.searchByNombreContains("Lu");

        assertEquals(2, lista.size());
    }

    @Test
    void testBajaMascota() {
        Usuario usuario = new Usuario("Clara", "Lopez", "clara@mail.com",
                "1234", "6666666", "Centro", "Ciudad");
        usuarioDAO.persist(usuario);

        Estado estado = new Estado("ENCONTRADO");
        estadoDAO.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.62, -58.37, "Norte");
        ubicacionDAO.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Rocky", "Grande",
                "Negro", LocalDate.now(), ubicacion, estado, "Mascota tranquila");
        mascotaDAO.persist(mascota);

        mascotaDAO.delete(mascota);

        Mascota encontrada = mascotaDAO.get(mascota.getId());
        assertNull(encontrada);
    }

    @Test
    void testActualizacionMascota() {
        Usuario usuario = new Usuario("Pedro", "Gomez", "pedro@mail.com",
                "1234", "7777777", "Sur", "Ciudad");
        usuarioDAO.persist(usuario);

        Estado estado = new Estado("PERDIDO");
        estadoDAO.persist(estado);

        Ubicacion ubicacion = new Ubicacion(-34.63, -58.36, "Sur");
        ubicacionDAO.persist(ubicacion);

        Mascota mascota = new Mascota(usuario, "Toby", "Mediano",
                "Marron", LocalDate.now(), ubicacion, estado, "Mascota activa");
        mascotaDAO.persist(mascota);

        // Actualizar nombre y tamaño
        mascota.setNombre("Toby Updated");
        mascota.setTamano("Grande");
        mascotaDAO.update(mascota);

        Mascota encontrada = mascotaDAO.get(mascota.getId());
        assertEquals("Toby Updated", encontrada.getNombre());
        assertEquals("Grande", encontrada.getTamano());
    }
}
