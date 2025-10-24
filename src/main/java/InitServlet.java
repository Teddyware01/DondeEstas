import dondeestas.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import persistencia.DAO.FactoryDAO;
import persistencia.DAO.UsuarioDAO;
import persistencia.EMF;

@WebServlet("/init")
public class InitServlet extends HttpServlet {
    private EntityManager em = EMF.getEMF().createEntityManager();

    @Override
    public void init() {
        UsuarioDAO dao = FactoryDAO.getUsuarioDAO(em);
        Usuario u = new Usuario("Juan", "Perez", "juan@ejemplo.com",
                "1234", "1111", "Centro", "La Plata");
        dao.persist(u);
        System.out.println("Usuario insertado desde servlet.");
    }
}