package dondeestas;

import org.junit.jupiter.api.*;
import persistencia.DAO.FactoryDAO;
import persistencia.EMF;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MascotaTest {
    private static Usuario USUARIO_NUEVO;
    private static Mascota MASCOTA_NUEVO1;
    private static Mascota MASCOTA_NUEVO2;
    private static Estado PERDIDO;
    private static Estado RECUPERADO;
    private static Ubicacion CENTRO;
    private static Ubicacion NORTE;

    @BeforeAll
    static void init() {
        USUARIO_NUEVO = Usuario.crearYGuardar("Martin", "Martines", "martin@gmail.com", "pass", "111", "Centro", "CABA");

        PERDIDO = new Estado("PERDIDO");
        RECUPERADO = new Estado("RECUPERADO");
        FactoryDAO.getEstadoDAO().persist(PERDIDO);
        FactoryDAO.getEstadoDAO().persist(RECUPERADO);

        CENTRO = new Ubicacion(-34.60, -58.38, "Centro");
        NORTE = new Ubicacion(-34.55, -58.40, "Norte");
        FactoryDAO.getUbicacionDAO().persist(CENTRO);
        FactoryDAO.getUbicacionDAO().persist(NORTE);

        MASCOTA_NUEVO1 = Mascota.crearYGuardar(USUARIO_NUEVO, "Tobi", "Mediano", "Marron", LocalDate.now().minusDays(1), CENTRO, PERDIDO, "Mascota nueva 1");
        MASCOTA_NUEVO2 = Mascota.crearYGuardar(USUARIO_NUEVO, "Nico", "Chico", "Blanco", LocalDate.now().minusDays(5), CENTRO, PERDIDO, "Mascota nueva 2");
    }

    @AfterEach
    void limpiarDatos() {
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.createQuery("DELETE FROM Avistamiento").executeUpdate();

                em.createQuery("DELETE FROM Mascota WHERE id NOT IN (:id1, :id2)")
                        .setParameter("id1", MASCOTA_NUEVO1.getId())
                        .setParameter("id2", MASCOTA_NUEVO2.getId())
                        .executeUpdate();

                em.createQuery("DELETE FROM Usuario WHERE id <> :userId")
                        .setParameter("userId", USUARIO_NUEVO.getId())
                        .executeUpdate();

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
    void testGetMascotaPorId() {
        Mascota buscada = Mascota.getMascota(MASCOTA_NUEVO1.getId());
        assertNotNull(buscada);
        assertEquals("Tobi", buscada.getNombre());
    }

    @Test
    void testAltaMascota() {
        Mascota nuevaMascota = Mascota.crearYGuardar(USUARIO_NUEVO, "AltaTest", "Mediano", "Marron", LocalDate.now(), NORTE, PERDIDO, "Sin collar");

        assertNotNull(nuevaMascota.getId());
        Mascota encontrada = Mascota.getMascota(nuevaMascota.getId());
        assertNotNull(encontrada);
        assertEquals("AltaTest", encontrada.getNombre());
    }

    @Test
    void testBajaMascota() {
        Usuario usuarioTemp = Usuario.crearYGuardar("hola", "capo", "hola@gmail.com", "pass", "444", "Sur", "CABA");
        Mascota mascota1 = Mascota.crearYGuardar(usuarioTemp, "Temporal", "Chico", "Blanco", LocalDate.now(), NORTE, PERDIDO, "Para borrar");
        Long id = mascota1.getId();

        mascota1.borrarMascota();

        assertNull(Mascota.getMascota(id));
    }

    @Test
    void testActualizacionMascota() {
        Mascota mascota2 = Mascota.crearYGuardar(USUARIO_NUEVO, "Temporal", "Chico", "Blanco", LocalDate.now(), NORTE, PERDIDO, "Para actualizar");

        Mascota aModificar = Mascota.getMascota(mascota2.getId());

        aModificar.setNombre("Temp Modificado");
        aModificar.setTamano("Grande");
        Mascota.guardarMascota(aModificar);

        Mascota encontrada = Mascota.getMascota(mascota2.getId());
        assertEquals("Temp Modificado", encontrada.getNombre());
        assertEquals("Grande", encontrada.getTamano());
    }

    @Test
    void testActualizarEstado() {
        Mascota aModificar = Mascota.getMascota(MASCOTA_NUEVO2.getId());

        aModificar.actualizarEstado(RECUPERADO);

        Mascota encontrada = Mascota.getMascota(MASCOTA_NUEVO2.getId());
        assertEquals(RECUPERADO.getNombreEstado(), encontrada.getEstado().getNombreEstado());
    }

    @Test
    void testCrearAvistamiento() {
        Avistamiento av = MASCOTA_NUEVO1.registrarAvistamiento("foto1.jpg", LocalDateTime.now(), "Visto en el parque", USUARIO_NUEVO, NORTE);

        assertNotNull(av.getId());

        Mascota mascotaConAvistamientos = Mascota.getMascota(MASCOTA_NUEVO1.getId());
        assertEquals(1, mascotaConAvistamientos.verAvistamientos().size());
        assertEquals("Visto en el parque", mascotaConAvistamientos.verAvistamientos().getFirst().getComentario());
    }

    @Test
    void testFindByEstado() {
        List<Mascota> perdidas = Mascota.findByEstado(PERDIDO);
        List<Mascota> recuperadas = Mascota.findByEstado(RECUPERADO);

        assertEquals(2, perdidas.size());
        assertEquals(0, recuperadas.size());
    }

    @Test
    void testFindByUsuario() {
        Usuario usuarioOtro = Usuario.crearYGuardar("OtroBusq", "User", "otro@gmail.com", "pass", "333", "Sur", "CABA");
        Mascota.crearYGuardar(usuarioOtro, "Milo", "Negro", "Negro", LocalDate.now(), NORTE, PERDIDO, "...");

        List<Mascota> listaBase = Mascota.findByUsuario(USUARIO_NUEVO.getId());
        List<Mascota> listaOtro = Mascota.findByUsuario(usuarioOtro.getId());

        assertEquals(2, listaBase.size());
        assertEquals(1, listaOtro.size());
    }

    @Test
    void testFindByBarrio() {
        Mascota.crearYGuardar(USUARIO_NUEVO, "Nina", "Grande", "Blanco", LocalDate.now(), NORTE, PERDIDO, "...");

        List<Mascota> listaCentro = Mascota.findByBarrio("Centro");
        List<Mascota> listaNorte = Mascota.findByBarrio("Norte");

        assertEquals(2, listaCentro.size());
        assertEquals(1, listaNorte.size());
    }

    @Test
    void testSearchByNombreContains() {
        Mascota.crearYGuardar(USUARIO_NUEVO, "Luna", "Pequeño", "Blanco", LocalDate.now(), CENTRO, PERDIDO, "...");
        Mascota.crearYGuardar(USUARIO_NUEVO, "Lucho", "Mediano", "Negro", LocalDate.now(), CENTRO, PERDIDO, "...");

        List<Mascota> lista = Mascota.searchByNombreContains("Lu");

        assertEquals(2, lista.size());
        assertTrue(lista.stream().anyMatch(m -> m.getNombre().equals("Luna")));
        assertTrue(lista.stream().anyMatch(m -> m.getNombre().equals("Lucho")));
    }
}
