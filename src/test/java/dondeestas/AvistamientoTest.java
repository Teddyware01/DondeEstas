package dondeestas;

import org.junit.jupiter.api.*;
import persistencia.DAO.FactoryDAO;
import persistencia.EMF;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import persistencia.hibernate.EstadoDAOHibernateJPA;
import persistencia.hibernate.MascotaDAOHibernateJPA;
import persistencia.hibernate.UbicacionDAOHibernateJPA;
import persistencia.hibernate.UsuarioDAOHibernateJPA;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AvistamientoTest {
    private static Usuario USUARIO_REPORTERO;
    private static Usuario USUARIO_DUENO;
    private static Mascota MASCOTA_BASE;
    private static Estado ESTADO_PERDIDO;
    private static Ubicacion UBICACION_CENTRO;
    private static Ubicacion UBICACION_NORTE;

    private static MascotaDAOHibernateJPA mascotaDAO;
    private static UsuarioDAOHibernateJPA usuarioDAO;
    private static EstadoDAOHibernateJPA estadoDAO;
    private static UbicacionDAOHibernateJPA ubicacionDAO;

    @BeforeAll
    static void init() {
        mascotaDAO = (MascotaDAOHibernateJPA) FactoryDAO.getMascotaDAO();
        usuarioDAO = (UsuarioDAOHibernateJPA) FactoryDAO.getUsuarioDAO();
        estadoDAO = (EstadoDAOHibernateJPA) FactoryDAO.getEstadoDAO();
        ubicacionDAO = (UbicacionDAOHibernateJPA) FactoryDAO.getUbicacionDAO();

        USUARIO_REPORTERO = Usuario.crearYGuardar("Reportero", "Test", "report@mail.com", "pass", "111", "Centro", "CABA");
        USUARIO_DUENO = Usuario.crearYGuardar("Dueno", "Test", "dueno@mail.com", "pass", "222", "Norte", "CABA");

        ESTADO_PERDIDO = new Estado("PERDIDO");
        FactoryDAO.getEstadoDAO().persist(ESTADO_PERDIDO);

        UBICACION_CENTRO = new Ubicacion(-34.60, -58.38, "Centro");
        UBICACION_NORTE = new Ubicacion(-34.55, -58.40, "Norte");
        FactoryDAO.getUbicacionDAO().persist(UBICACION_CENTRO);
        FactoryDAO.getUbicacionDAO().persist(UBICACION_NORTE);

        MASCOTA_BASE = Mascota.crearYGuardar(USUARIO_DUENO, "Tobi", "Mediano", "Marron",
                LocalDate.now().minusDays(10), UBICACION_CENTRO, ESTADO_PERDIDO, "Chiwawa");
    }

    @AfterEach
    void limpiarDatos() {
        try (EntityManager em = EMF.getEMF().createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.createQuery("DELETE FROM Avistamiento").executeUpdate();

                em.createQuery("DELETE FROM Mascota WHERE id <> :mascotaId")
                        .setParameter("mascotaId", MASCOTA_BASE.getId())
                        .executeUpdate();

                em.createQuery("DELETE FROM Usuario WHERE id <> :reporteroId AND id <> :duenoId")
                        .setParameter("reporteroId", USUARIO_REPORTERO.getId())
                        .setParameter("duenoId", USUARIO_DUENO.getId())
                        .executeUpdate();

                em.createQuery("DELETE FROM Ubicacion WHERE id <> :centroId AND id <> :norteId")
                        .setParameter("centroId", UBICACION_CENTRO.getId())
                        .setParameter("norteId", UBICACION_NORTE.getId())
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
    void testAltaAvistamiento() {
        Avistamiento nuevoAv = Avistamiento.crearYGuardar("foto1.jpg", LocalDateTime.now(),
                "Visto en la esquina", MASCOTA_BASE, USUARIO_REPORTERO, UBICACION_NORTE);

        assertNotNull(nuevoAv.getId());
        Avistamiento encontrado = Avistamiento.getAvistamiento(nuevoAv.getId());
        assertNotNull(encontrado);
        assertEquals("Visto en la esquina", encontrado.getComentario());
    }

    @Test
    void testGetAvistamientoPorId() {
        Avistamiento av = Avistamiento.crearYGuardar("foto_get.jpg", LocalDateTime.now(),
                "Para buscar por ID", MASCOTA_BASE, USUARIO_REPORTERO, UBICACION_CENTRO);

        Avistamiento buscado = Avistamiento.getAvistamiento(av.getId());

        assertNotNull(buscado);
        assertEquals(USUARIO_REPORTERO.getEmail(), buscado.getUsuario().getEmail());
    }

    @Test
    void testActualizacionAvistamiento() {
        Avistamiento av = Avistamiento.crearYGuardar("foto_update.jpg", LocalDateTime.now(),
                "Comentario Inicial", MASCOTA_BASE, USUARIO_REPORTERO, UBICACION_CENTRO);

        av.setComentario("Comentario Modificado");
        av.guardarAvistamiento();

        Avistamiento encontrado = Avistamiento.getAvistamiento(av.getId());
        assertEquals("Comentario Modificado", encontrado.getComentario());
    }

    @Test
    void testBajaAvistamiento() {
        Avistamiento av = Avistamiento.crearYGuardar("foto_delete.jpg", LocalDateTime.now(),
                "Para ser borrado", MASCOTA_BASE, USUARIO_REPORTERO, UBICACION_NORTE);
        Long id = av.getId();

        av.borrarAvistamiento();

        assertNotNull(Avistamiento.getAvistamiento(id));
    }

    @Test
    void testFindByUsuario() {
        Avistamiento.crearYGuardar("foto_u2_1.jpg", LocalDateTime.now(), "Reporte 1 U2", MASCOTA_BASE, USUARIO_DUENO, UBICACION_CENTRO);

        Avistamiento.crearYGuardar("foto_u1_1.jpg", LocalDateTime.now(), "Reporte 1 U1", MASCOTA_BASE, USUARIO_REPORTERO, UBICACION_CENTRO);
        Avistamiento.crearYGuardar("foto_u1_2.jpg", LocalDateTime.now(), "Reporte 2 U1", MASCOTA_BASE, USUARIO_REPORTERO, UBICACION_NORTE);


        List<Avistamiento> reportesU1 = Avistamiento.findByUsuario(USUARIO_REPORTERO);
        List<Avistamiento> reportesU2 = Avistamiento.findByUsuario(USUARIO_DUENO);

        assertEquals(2, reportesU1.size());
        assertEquals(1, reportesU2.size());
    }

    @Test
    void testFindByMascota() {
        Usuario usuarioTemp = Usuario.crearYGuardar("temp", "user", "temp@mail.com", "pass", "333", "Temp", "CABA");
        Mascota mascotaTemp = Mascota.crearYGuardar(usuarioTemp, "Temp", "Pequeño", "Gris",
                LocalDate.now(), UBICACION_NORTE, ESTADO_PERDIDO, "Temporal");

        Avistamiento.crearYGuardar("foto_m1_1.jpg", LocalDateTime.now(), "Avist. M1", MASCOTA_BASE, USUARIO_REPORTERO, UBICACION_CENTRO);
        Avistamiento.crearYGuardar("foto_m1_2.jpg", LocalDateTime.now(), "Avist. M1", MASCOTA_BASE, USUARIO_DUENO, UBICACION_NORTE);

        List<Avistamiento> listaBase = Avistamiento.findByMascota(MASCOTA_BASE);
        List<Avistamiento> listaTemp = Avistamiento.findByMascota(mascotaTemp);

        assertEquals(2, listaBase.size());
        assertEquals(0, listaTemp.size());

        mascotaTemp.borrarMascota();
        usuarioTemp.borrarUsuario();
    }
}
