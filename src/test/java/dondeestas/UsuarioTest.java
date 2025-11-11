package dondeestas;

import dondeestas.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import persistencia.DAO.FactoryDAO;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {


    private static Usuario usuario1, usuario2,usuarioParaBorrar;
    private static Puntaje puntaje1, puntaje2;
    private static Medalla medalla1;

    @BeforeAll
    static void init() {
        usuario1 = Usuario.crearYGuardar("Juan", "Perez", "juan@correo.com", "1234", "111", "Centro", "Ciudad");
        usuario2 = Usuario.crearYGuardar("Ana", "Lopez", "ana@correo.com", "5678", "222", "Norte", "Ciudad");
        usuarioParaBorrar = Usuario.crearYGuardar("Ricardo","Sand", "arena@gmail.com","pass", "123", "Palermo", "Ciudad");
        puntaje1 = new Puntaje(10, "Avistamiento");
        puntaje2 = new Puntaje(20, "Adopcion");
        medalla1 = new Medalla("Colaborador");

        FactoryDAO.getPuntajeDAO().persist(puntaje1);
        FactoryDAO.getPuntajeDAO().persist(puntaje2);
        FactoryDAO.getMedallaDAO().persist(medalla1);

    }

    @Test
    void altaUsuario() {
        Usuario usuario = Usuario.crearYGuardar("Pedro", "Gomez", "pedro@correo.com", "9988", "333", "Sur", "Ciudad");
        assertNotNull(usuario.getId());
        Usuario encontrado = FactoryDAO.getUsuarioDAO().get(usuario.getId());
        assertNotNull(encontrado);
        assertEquals(usuario.getEmail(), encontrado.getEmail());
        assertEquals("Gomez", usuario.getApellido());
    }

    @Test
    void eliminarUsuario() {
        assertNotNull(Usuario.getUsuario(usuarioParaBorrar.getId()));
        Long id = usuarioParaBorrar.getId();
        usuarioParaBorrar.borrarUsuario();
        assertNull(Usuario.getUsuario(id));
    }

    @Test
    void editarUsuario() {
        usuario1.editarUsuario("Jose", "Martinez", "jose@correo.com", "abcd", "444", "Este", "CiudadX");
        assertEquals("Jose", usuario1.getNombre());
        assertEquals("Martinez", usuario1.getApellido());
        assertEquals("jose@correo.com", usuario1.getEmail());
        assertEquals("444", usuario1.getTelefono());
    }


    @Test
    void crearMascota() {
        Ubicacion ubic = new Ubicacion(12.23,2.45, "Barrio");
        Estado estado = new Estado("Perdido");
        FactoryDAO.getUbicacionDAO().persist(ubic);
        FactoryDAO.getEstadoDAO().persist(estado);
        Mascota mascota = usuario1.crearMascota("Firulais", "Mediano", "Marron", LocalDate.now(), ubic, estado, "Sin collar");
        assertNotNull(mascota.getId());
        assertEquals("Firulais", usuario1.verMascotas().getFirst().getNombre());
        assertEquals(1, usuario1.verMascotas().size());
    }

    @Test
    void verMascotasVacio() {
        assertTrue(usuario2.verMascotas().isEmpty());
    }

    @Test
    void usuarioConPuntos() {
        usuario1.agregarPuntaje(puntaje1);
        usuario1.agregarPuntaje(puntaje2);
        assertEquals(30, usuario1.getTotalPuntos());
    }

    @Test
    void usuarioSinPuntos() {
        assertEquals(0, usuario2.getTotalPuntos());
    }

    @Test
    void agregarMedalla() {
        usuario1.agregarMedalla(medalla1);
        assertEquals(1, usuario1.verMedallas().size());
    }

    @Test
    void usuarioConMedallas(){
        ArrayList<Medalla> medallasDeUsu1 = (ArrayList<Medalla>) usuario1.verMedallas();
        assertEquals(1, medallasDeUsu1.size());
        assertEquals(medalla1,medallasDeUsu1.getFirst());

    }
    @Test
    void usuarioSinMedallas(){
        ArrayList<Medalla> medallasDeUsu2 = (ArrayList<Medalla>) usuario2.verMedallas();
        assertEquals(0, medallasDeUsu2.size());
        usuario2.agregarMedalla(medalla1);
        assertEquals(1, usuario2.verMedallas().size());
        assertEquals(medalla1,usuario2.verMedallas().getFirst());
    }

    @Test
    void getUsuarioPorId() {
        Usuario buscado = Usuario.getUsuario(usuario1.getId());
        assertNotNull(buscado);
        assertEquals(usuario1.getEmail(), buscado.getEmail());
    }
}
