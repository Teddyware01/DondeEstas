package persistencia.hibernate;

import dondeestas.Puntaje;
import dondeestas.Usuario;
import dondeestas.UsuarioPuntaje;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import persistencia.EMF;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioPuntajeDAOHibernateJPATest {

    private static EntityManager em;
    private static UsuarioPuntajeDAOHibernateJPA usuarioPuntajeDAO;

    @BeforeAll
    static void setUp() {
        em = EMF.getEMF().createEntityManager();
        usuarioPuntajeDAO = new UsuarioPuntajeDAOHibernateJPA();
    }

    @BeforeEach
    void iniciarTransaccion() {
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
        em.createQuery("DELETE FROM UsuarioPuntaje").executeUpdate();
        em.createQuery("DELETE FROM Usuario").executeUpdate();
        em.createQuery("DELETE FROM Puntaje").executeUpdate();
        tx.commit();
    }

    @Test
    void testAltaUsuarioPuntaje() {
        Usuario usuario = new Usuario("Juan", "Perez", "juan@mail.com", "1234", "1234567890", "Centro", "CiudadX");
        Puntaje puntaje = new Puntaje(10, "Publicar Avistamiento");
        em.persist(usuario);
        em.persist(puntaje);

        UsuarioPuntaje up = new UsuarioPuntaje(usuario, puntaje, LocalDate.now());
        UsuarioPuntaje guardado = usuarioPuntajeDAO.persist(up);

        assertNotNull(guardado.getId());
        assertEquals(10, guardado.getPuntaje().getCantidad());
    }

    @Test
    void testBajaUsuarioPuntaje() {
        Usuario usuario = new Usuario("Ana", "Lopez", "ana@mail.com", "pass", "9876543210", "Norte", "CiudadY");
        Puntaje puntaje = new Puntaje(5, "Registrar Mascota");
        em.persist(usuario);
        em.persist(puntaje);

        UsuarioPuntaje up = new UsuarioPuntaje(usuario, puntaje, LocalDate.now());
        em.persist(up);

        usuarioPuntajeDAO.delete(up);

        UsuarioPuntaje encontrado = usuarioPuntajeDAO.get(up.getId());
        assertNull(encontrado);
    }

    @Test
    void testActualizacionUsuarioPuntaje() {
        Usuario usuario = new Usuario("Pedro", "Martinez", "pedro@mail.com", "1234", "111222333", "Centro", "CiudadX");
        Puntaje p1 = new Puntaje(10, "Subir Foto");
        Puntaje p2 = new Puntaje(5, "Comentar");
        em.persist(usuario);
        em.persist(p1);
        em.persist(p2);

        UsuarioPuntaje up = new UsuarioPuntaje(usuario, p1, LocalDate.now());
        em.persist(up);

        // Actualizamos el puntaje a otro tipo
        up.setPuntaje(p2);
        usuarioPuntajeDAO.update(up);

        UsuarioPuntaje encontrado = usuarioPuntajeDAO.get(up.getId());
        assertEquals(5, encontrado.getPuntaje().getCantidad());
    }

    @Test
    void testFindByUsuario() {
        Usuario usuario = new Usuario("Ana", "Lopez", "ana@mail.com", "pass", "9876543210", "Norte", "CiudadY");
        Usuario otro = new Usuario("Luis", "Diaz", "luis@mail.com", "123", "1122334455", "Sur", "CiudadZ");
        Puntaje puntaje = new Puntaje(5, "Registrar Mascota");

        em.persist(usuario);
        em.persist(otro);
        em.persist(puntaje);

        UsuarioPuntaje up = new UsuarioPuntaje(usuario, puntaje, LocalDate.now());
        em.persist(up);

        List<UsuarioPuntaje> lista = usuarioPuntajeDAO.findByUsuario(usuario.getId());

        assertEquals(1, lista.size());
        assertEquals(usuario.getId(), lista.get(0).getUsuario().getId());
    }

    @Test
    void testGetTotalPuntosByUsuario() {
        Usuario usuario = new Usuario("Pedro", "Martinez", "pedro@mail.com", "1234", "111222333", "Centro", "CiudadX");
        Puntaje p1 = new Puntaje(10, "Subir Foto");
        Puntaje p2 = new Puntaje(5, "Comentar");
        em.persist(usuario);
        em.persist(p1);
        em.persist(p2);

        em.persist(new UsuarioPuntaje(usuario, p1, LocalDate.now()));
        em.persist(new UsuarioPuntaje(usuario, p2, LocalDate.now()));

        Integer total = usuarioPuntajeDAO.getTotalPuntosByUsuario(usuario.getId());

        assertEquals(15, total);
    }

    @Test
    void testFindByTipo() {
        Usuario usuario = new Usuario("Sofia", "Diaz", "sofia@mail.com", "pass1", "123456789", "Centro", "CiudadX");
        Puntaje puntaje = new Puntaje(20, "Ayuda a otro usuario");
        em.persist(usuario);
        em.persist(puntaje);

        UsuarioPuntaje up = new UsuarioPuntaje(usuario, puntaje, LocalDate.now());
        em.persist(up);

        List<UsuarioPuntaje> lista = usuarioPuntajeDAO.findByTipo(puntaje);

        assertEquals(1, lista.size());
        assertEquals("Ayuda a otro usuario", lista.get(0).getPuntaje().getTipoPuntaje());
    }
}
