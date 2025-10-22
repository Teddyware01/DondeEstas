package persistencia.hibernate;

import dondeestas.Medalla;
import dondeestas.Usuario;
import dondeestas.UsuarioMedalla;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import persistencia.EMF;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMedallaDAOHibernateJPATest {

    private static EntityManager em;
    private static UsuarioMedallaDAOHibernateJPA usuarioMedallaDAO;

    private Medalla contribuidor;
    private Medalla experto;

    @BeforeAll
    static void setUp() {
        em = EMF.getEMF().createEntityManager();
        usuarioMedallaDAO = new UsuarioMedallaDAOHibernateJPA();
    }

    @BeforeEach
    void iniciarTransaccion() {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }

        // Crear medallas fijas para tests
        contribuidor = new Medalla("Contribuidor");
        experto = new Medalla("Experto");
        em.persist(contribuidor);
        em.persist(experto);
    }

    @AfterEach
    void limpiarDatos() {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive()) {
            tx.begin();
        }
        em.createQuery("DELETE FROM UsuarioMedalla").executeUpdate();
        em.createQuery("DELETE FROM Usuario").executeUpdate();
        em.createQuery("DELETE FROM Medalla").executeUpdate();
        tx.commit();
    }

    @Test
    void testAltaUsuarioMedalla() {
        Usuario usuario = new Usuario("Juan", "Perez", "juan@mail.com", "1234", "1234567890", "Centro", "CiudadX");
        em.persist(usuario);

        UsuarioMedalla um = new UsuarioMedalla(usuario, contribuidor);
        UsuarioMedalla guardada = usuarioMedallaDAO.persist(um);

        assertNotNull(guardada.getId());
        assertEquals("Contribuidor", guardada.getMedalla().getNombreMedalla());
    }

    @Test
    void testBajaUsuarioMedalla() {
        Usuario usuario = new Usuario("Ana", "Lopez", "ana@mail.com", "pass", "9876543210", "Norte", "CiudadY");
        em.persist(usuario);

        UsuarioMedalla um = new UsuarioMedalla(usuario, experto);
        em.persist(um);

        // Borrar la asociación
        usuarioMedallaDAO.delete(um);

        UsuarioMedalla encontrada = usuarioMedallaDAO.get(um.getId());
        assertNull(encontrada);
    }

    @Test
    void testActualizacionUsuarioMedalla() {
        Usuario usuario = new Usuario("Pedro", "Martinez", "pedro@mail.com", "1234", "111222333", "Centro", "CiudadX");
        em.persist(usuario);

        UsuarioMedalla um = new UsuarioMedalla(usuario, contribuidor);
        em.persist(um);

        // Cambiamos la medalla a experto
        um.setMedalla(experto);
        usuarioMedallaDAO.update(um);

        UsuarioMedalla encontrada = usuarioMedallaDAO.get(um.getId());
        assertEquals("Experto", encontrada.getMedalla().getNombreMedalla());
    }

    @Test
    void testFindByUsuario() {
        Usuario usuario = new Usuario("Ana", "Lopez", "ana@mail.com", "pass", "9876543210", "Norte", "CiudadY");
        Usuario otro = new Usuario("Luis", "Diaz", "luis@mail.com", "123", "1122334455", "Sur", "CiudadZ");
        em.persist(usuario);
        em.persist(otro);

        UsuarioMedalla um = new UsuarioMedalla(usuario, contribuidor);
        em.persist(um);

        List<UsuarioMedalla> lista = usuarioMedallaDAO.findByUsuario(usuario.getId());

        assertEquals(1, lista.size());
        assertEquals(usuario.getId(), lista.get(0).getUsuario().getId());
    }

    @Test
    void testUsuarioTieneMedalla() {
        Usuario usuario = new Usuario("Pedro", "Martinez", "pedro@mail.com", "1234", "111222333", "Centro", "CiudadX");
        em.persist(usuario);

        UsuarioMedalla um = new UsuarioMedalla(usuario, experto);
        em.persist(um);

        boolean tiene = usuarioMedallaDAO.usuarioTieneMedalla(usuario.getId(), experto);
        boolean noTiene = usuarioMedallaDAO.usuarioTieneMedalla(usuario.getId(), contribuidor);

        assertTrue(tiene);
        assertFalse(noTiene);
    }

    @Test
    void testFindByTipo() {
        Usuario usuario1 = new Usuario("Sofia", "Diaz", "sofia@mail.com", "pass1", "123456789", "Centro", "CiudadX");
        Usuario usuario2 = new Usuario("Mario", "Lopez", "mario@mail.com", "pass2", "987654321", "Sur", "CiudadY");
        em.persist(usuario1);
        em.persist(usuario2);

        UsuarioMedalla um1 = new UsuarioMedalla(usuario1, experto);
        UsuarioMedalla um2 = new UsuarioMedalla(usuario2, experto);
        em.persist(um1);
        em.persist(um2);

        List<UsuarioMedalla> lista = usuarioMedallaDAO.findByTipo(experto);

        assertEquals(2, lista.size());
    }
}
