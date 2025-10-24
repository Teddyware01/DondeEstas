package persistencia.hibernate;

import dondeestas.Ubicacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import persistencia.EMF;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UbicacionDAOHibernateJPATest {

    private static EntityManager em;
    private static UbicacionDAOHibernateJPA ubicacionDAO;

    @BeforeAll
    static void inicializar() {
        em = EMF.getEMF().createEntityManager();
        ubicacionDAO = new UbicacionDAOHibernateJPA(em);
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
        em.createQuery("DELETE FROM Ubicacion").executeUpdate();
        tx.commit();
    }

    @Test
    void testPersistYGet() {
        // CORRECCIÓN: usar el constructor correcto (latitud, longitud, barrio)
        Ubicacion ubicacion = new Ubicacion(-34.578, -58.425, "Palermo");

        // Persistir
        Ubicacion guardada = ubicacionDAO.persist(ubicacion);

        // Obtener por ID
        Ubicacion encontrada = ubicacionDAO.get(guardada.getId());

        assertNotNull(encontrada);
        assertEquals("Palermo", encontrada.getBarrio());
        assertEquals(-34.578, encontrada.getLatitud());
        assertEquals(-58.425, encontrada.getLongitud());
    }

    @Test
    void testFindByBarrio() {
        Ubicacion u1 = new Ubicacion(-34.56, -58.44, "Belgrano");
        Ubicacion u2 = new Ubicacion(-34.55, -58.45, "Belgrano R");

        ubicacionDAO.persist(u1);
        ubicacionDAO.persist(u2);

        List<Ubicacion> lista = ubicacionDAO.findByBarrio("Belgrano");

        assertTrue(lista.size() >= 1);
        assertTrue(lista.stream().anyMatch(u -> u.getBarrio().equals("Belgrano")));
    }

    @Test
    void testFindWithinRadio() {
        double refLat = -34.6037;
        double refLon = -58.3816;

        Ubicacion cerca = new Ubicacion(-34.6030, -58.3820, "Centro"); // Menos de 1 km
        Ubicacion lejos = new Ubicacion(-34.9214, -57.9544, "La Plata"); // Más de 40 km

        ubicacionDAO.persist(cerca);
        ubicacionDAO.persist(lejos);

        List<Ubicacion> lista = ubicacionDAO.findWithinRadio(refLat, refLon, 5.0);

        assertEquals(1, lista.size());
        assertEquals("Centro", lista.get(0).getBarrio());
    }
}
