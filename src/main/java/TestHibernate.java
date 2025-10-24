import jakarta.persistence.EntityManager;
import persistencia.DAO.FactoryDAO;
import persistencia.DAO.UsuarioDAO;
import dondeestas.Usuario;
import persistencia.EMF;

public class TestHibernate {
    private static EntityManager em = EMF.getEMF().createEntityManager();

    public static void main(String[] args) {
        UsuarioDAO dao = FactoryDAO.getUsuarioDAO(em);
        Usuario u = new Usuario("Juan", "Perez", "juan@ejemplo.com",
                "1234", "1111", "Centro", "La Plata");
        dao.persist(u);
        System.out.println("Usuario insertado, tablas creadas.");
    }
}
