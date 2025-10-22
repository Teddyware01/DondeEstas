package persistencia.hibernate;

import dondeestas.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import persistencia.EMF;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOHibernateJPATest {

    private static EntityManager em;
    private static UsuarioDAOHibernateJPA usuarioDAO;

    @BeforeAll
    static void inicializar() {
        em = EMF.getEMF().createEntityManager();
        usuarioDAO = new UsuarioDAOHibernateJPA();
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
        em.createQuery("DELETE FROM Usuario").executeUpdate();
        tx.commit();
    }

    @Test
    void testGetByEmailNotFound() {
        Usuario fetched = usuarioDAO.getByEmail("noexiste@example.com");
        assertNull(fetched);
    }

    @Test
    void testSaveAndGetByEmail() {
        Usuario u = new Usuario("Juan", "Perez", "juan@example.com", "1234", "1234567890", "Centro", "CiudadX");
        usuarioDAO.persist(u);

        Usuario fetched = usuarioDAO.getByEmail("juan@example.com");
        assertNotNull(fetched);
        assertEquals("juan@example.com", fetched.getEmail());
        assertEquals("1234567890", fetched.getTelefono());
    }

    @Test
    void testUpdateUsuario() {
        Usuario u = new Usuario("Ana", "Lopez", "ana@example.com", "pass", "9876543210", "Norte", "CiudadY");
        usuarioDAO.persist(u);

        u.setApellido("Antonic");
        usuarioDAO.update(u);

        Usuario fetched = usuarioDAO.getByEmail("ana@example.com");
        assertEquals("Antonic", fetched.getApellido());
    }

    /*************************************
     * ver la opcion de meter un try catch:
     * public T update(T entity) {
     * EntityManager em = EMF.getEMF().createEntityManager();
     * EntityTransaction tx = em.getTransaction();
     * T merged = null;
     * try {
     *  tx.begin();
     *  merged = em.merge(entity);
     *  tx.commit();
     * } catch (RuntimeException e) {
     *  if (tx.isActive())
     *      tx.rollback();
     *      throw e;
     *  } finally {
     *      em.close();
     *  }
     *  return merged;
     *  }
     */

    @Test
    void testDeleteUsuario() {
        Usuario u = new Usuario("Carlos", "Gomez", "carlos@example.com", "123", "1122334455", "Sur", "CiudadZ");
        usuarioDAO.persist(u); // <<-- persist

        usuarioDAO.delete(u);
        Usuario fetched = usuarioDAO.getByEmail("carlos@example.com");
        assertNull(fetched);
    }

}
