    package persistencia.hibernate;

    import dondeestas.entity.*;
    import org.junit.jupiter.api.*;
    import persistencia.DAO.AvistamientoDAO;
    import persistencia.DAO.FactoryDAO;
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.EntityTransaction;
    import persistencia.EMF;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.List;

    import static org.junit.jupiter.api.Assertions.*;

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class AvistamientoDAOHibernateJPATest {

        private static AvistamientoDAO avistamientoDAO;
        private static MascotaDAOHibernateJPA mascotaDAO;
        private static UsuarioDAOHibernateJPA usuarioDAO;
        private static EstadoDAOHibernateJPA estadoDAO;
        private static UbicacionDAOHibernateJPA ubicacionDAO;

        private static Usuario usuario;
        private static Estado estado;
        private static Ubicacion ubicacionMascota;
        private static Mascota mascota;
        private static Ubicacion ubicacionAvistamiento;
        private static Avistamiento avistamiento;

        @BeforeAll
        static void inicializar() {
            avistamientoDAO = FactoryDAO.getAvistamientoDAO();
            mascotaDAO = (MascotaDAOHibernateJPA) FactoryDAO.getMascotaDAO();
            usuarioDAO = (UsuarioDAOHibernateJPA) FactoryDAO.getUsuarioDAO();
            estadoDAO = (EstadoDAOHibernateJPA) FactoryDAO.getEstadoDAO();
            ubicacionDAO = (UbicacionDAOHibernateJPA) FactoryDAO.getUbicacionDAO();
        }

        @BeforeEach
        void setUp() {
            usuario = new Usuario("Juan","Perez","juan@mail.com","1234","123456789","Centro","Ciudad");
            usuarioDAO.persist(usuario);

            estado = new Estado("PERDIDO");
            estadoDAO.persist(estado);

            ubicacionMascota = new Ubicacion(-34.60,-58.38,"Centro");
            ubicacionDAO.persist(ubicacionMascota);

            mascota = new Mascota(usuario,"Firulais","Mediano","Marron", LocalDate.now(), ubicacionMascota, estado,"Mascota amistosa");
            mascotaDAO.persist(mascota);

            ubicacionAvistamiento = new Ubicacion(-34.61,-58.39,"Centro");
            ubicacionDAO.persist(ubicacionAvistamiento);

            avistamiento = new Avistamiento("foto1.png", LocalDateTime.now(), "Lo vi corriendo por calle 7 y 50", mascota, usuario, ubicacionAvistamiento);
            avistamientoDAO.persist(avistamiento);
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
        void testAltaAvistamiento() {
            Avistamiento avistamiento = new Avistamiento("foto_path", LocalDateTime.now(), "Lo vi corriendo por calle 7 y 50", mascota, usuario, ubicacionAvistamiento);
            Avistamiento guardado = avistamientoDAO.persist(avistamiento);

            Avistamiento encontrado = avistamientoDAO.get(guardado.getId());

            assertNotNull(encontrado);
            assertEquals(usuario.getEmail(), encontrado.getUsuario().getEmail());
            assertEquals("Lo vi corriendo por calle 7 y 50", encontrado.getComentario());
        }

        @Test
        void testFindById() {
            Avistamiento encontrado = avistamientoDAO.get(avistamiento.getId());

            assertNotNull(encontrado);
            assertEquals(mascota.getNombre(), encontrado.getMascota().getNombre());
        }

        @Test
        void testUpdateAvistamiento() {
            avistamiento.setComentario("Lo vi ahora en 10 y 48");
            avistamientoDAO.update(avistamiento);

            Avistamiento encontrado = avistamientoDAO.get(avistamiento.getId());
            assertEquals("Lo vi ahora en 10 y 48", encontrado.getComentario());
        }

        @Test
        void testBajaAvistamiento() {
            Long idAvistamiento = avistamiento.getId();

            avistamientoDAO.delete(avistamiento);

            Avistamiento encontrado = avistamientoDAO.get(idAvistamiento);
            assertNull(encontrado);
        }

        @Test
        void testFindByUsuario() {
            Usuario u1 = new Usuario("buscador1", "User", "buscador1@mail.com", "pass", "111", "Centro", "CABA");
            usuarioDAO.persist(u1);
            Usuario u2 = new Usuario("buscador2", "User", "buscador2@mail.com", "pass", "111", "Centro", "CABA");
            usuarioDAO.persist(u2);

            Avistamiento av1 = new Avistamiento("foto1.jpeg", LocalDateTime.now().minusHours(2), "Reporte 1 U1", mascota, u1, ubicacionAvistamiento);
            avistamientoDAO.persist(av1);
            Avistamiento av2 = new Avistamiento("foto2.jpeg", LocalDateTime.now().minusHours(1), "Reporte 2 U1", mascota, u1, ubicacionAvistamiento);
            avistamientoDAO.persist(av2);

            Avistamiento av3 = new Avistamiento("foto3.jpeg", LocalDateTime.now(), "Reporte 1 U2", mascota, u2, ubicacionAvistamiento);
            avistamientoDAO.persist(av3);

            List<Avistamiento> reportesU1 = avistamientoDAO.findByUsuario(u1);
            List<Avistamiento> reportesU2 = avistamientoDAO.findByUsuario(u2);

            assertEquals(2, reportesU1.size());
            assertEquals(1, reportesU2.size());
        }

        @Test
        void testFindByMascota() {
            Usuario duenio = new Usuario("duenio5", "User", "duenio5@mail.com", "pass", "111", "Centro", "CABA");
            usuarioDAO.persist(duenio);
            Usuario reporta = new Usuario("reporta5", "User", "reporta5@mail.com", "pass", "111", "Norte", "CABA");
            usuarioDAO.persist(reporta);

            Mascota m1 = new Mascota(duenio, "Milo", "Chico", "Blanco", LocalDate.now().minusDays(1), ubicacionMascota, estado, "Perdido");
            mascotaDAO.persist(m1);
            Mascota m2 = new Mascota(duenio, "Nala", "Grande", "Negro", LocalDate.now().minusDays(1), ubicacionMascota, estado, "Perdido");
            mascotaDAO.persist(m2);

            Avistamiento av1 = new Avistamiento("foto1.png", LocalDateTime.now().minusHours(2), "Reporte 1 M1", m1, reporta, ubicacionAvistamiento);
            avistamientoDAO.persist(av1);
            Avistamiento av2 = new Avistamiento("foto2.png", LocalDateTime.now().minusHours(1), "Reporte 2 M1", m1, reporta, ubicacionAvistamiento);
            avistamientoDAO.persist(av2);

            List<Avistamiento> avistamientosM1 = avistamientoDAO.findByMascota(m1.getId());
            List<Avistamiento> avistamientosM2 = avistamientoDAO.findByMascota(m2.getId());

            assertEquals(2, avistamientosM1.size());
            assertEquals(0, avistamientosM2.size());
        }
    }
