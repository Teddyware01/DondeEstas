package persistencia.hibernate;

import dondeestas.Usuario;
import org.junit.jupiter.api.*;
import persistencia.EMF;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOHibernateJPATest {

    private static UsuarioDAOHibernateJPA usuarioDAO;

    @BeforeAll
    static void inicializar() {
        usuarioDAO = new UsuarioDAOHibernateJPA();
    }

    // Usamos el AfterEach para LIMPIAR toda la tabla después de cada prueba.
    // Esta limpieza DEBE estar en su propia transacción.
    @AfterEach
    void limpiarDatos() {
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Ejecuta un comando SQL/JPQL de limpieza
            em.createQuery("DELETE FROM Usuario").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error limpiando datos de prueba", e);
        } finally {
            em.close();
        }
    }

    @Test
    void testGetByEmailNotFound() {
        // La prueba es simple, no necesita transacciones extra
        Usuario fetched = usuarioDAO.getByEmail("noexiste@example.com");
        assertNull(fetched);
    }

    @Test
    void testSaveAndGetByEmail() {
        // El persist() ya maneja la transacción interna
        Usuario u = new Usuario("Juan", "Perez", "juan@example.com", "1234", "1234567890", "Centro", "CiudadX");
        usuarioDAO.persist(u);

        Usuario fetched = usuarioDAO.getByEmail("juan@example.com");
        assertNotNull(fetched);
        assertEquals("juan@example.com", fetched.getEmail());
    }

    @Test
    void testUpdateUsuario() {
        // La operación de escritura se encapsula
        Usuario u = new Usuario("Ana", "Lopez", "ana@example.com", "pass", "9876543210", "Norte", "CiudadY");
        usuarioDAO.persist(u);

        // La operación de actualización se encapsula
        u.setApellido("Antonic");
        usuarioDAO.update(u);

        Usuario fetched = usuarioDAO.getByEmail("ana@example.com");
        assertEquals("Antonic", fetched.getApellido());
    }

    @Test
    void testDeleteUsuario() {
        Usuario u = new Usuario("Carlos", "Gomez", "carlos@example.com", "123", "1122334455", "Sur", "CiudadZ");
        usuarioDAO.persist(u);

        usuarioDAO.delete(u); // La eliminación se encapsula

        Usuario fetched = usuarioDAO.getByEmail("carlos@example.com");
        assertNull(fetched);
    }
}