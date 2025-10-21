/**
import dondeestas.Usuario;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import persistencia.EMF;
import persistencia.hibernate.UsuarioDAOHibernateJPA;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOHibernateJPATest {

    private static EntityManagerFactory emf;
    private UsuarioDAOHibernateJPA usuarioDAO;

    @BeforeAll
    static void initAll() {
        // Creamos un EntityManagerFactory para tests, usando una base H2 en memoria
        emf = Persistence.createEntityManagerFactory("testPU");
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        usuarioDAO = new UsuarioDAOHibernateJPA();
        // Aseguramos que EMF apunte al factory de test
    }

    @AfterEach
    void tearDown() {
        // Limpiar datos después de cada test
        var em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Usuario").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @Test
    void testSaveAndGetByEmail() {
        Usuario u = new Usuario("juan@example.com", "1234");
        usuarioDAO.save(u);

        Usuario fetched = usuarioDAO.getByEmail("juan@example.com");
        assertNotNull(fetched);
        assertEquals("juan@example.com", fetched.getEmail());
        assertEquals("1234", fetched.getPassword());
    }

    @Test
    void testGetByEmailNotFound() {
        Usuario fetched = usuarioDAO.getByEmail("noexiste@example.com");
        assertNull(fetched);
    }

    @Test
    void testUpdateUsuario() {
        Usuario u = new Usuario("ana@example.com", "pass");
        usuarioDAO.save(u);

        u.setPassword("nuevopass");
        usuarioDAO.update(u);

        Usuario fetched = usuarioDAO.getByEmail("ana@example.com");
        assertEquals("nuevopass", fetched.getPassword());
    }

    @Test
    void testDeleteUsuario() {
        Usuario u = new Usuario("carlos@example.com", "123");
        usuarioDAO.save(u);

        usuarioDAO.delete(u);
        Usuario fetched = usuarioDAO.getByEmail("carlos@example.com");
        assertNull(fetched);
    }
}
**/