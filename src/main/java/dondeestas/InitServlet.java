package dondeestas;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import persistencia.DAO.FactoryDAO;
import persistencia.DAO.UsuarioDAO;

@WebServlet("/init")
public class InitServlet extends HttpServlet {
    @Override
    public void init() {
        UsuarioDAO dao = FactoryDAO.getUsuarioDAO();
        Usuario u = new Usuario(null, "Juan", "Perez", "juan@ejemplo.com",
                "1234", "1111", "Centro", "La Plata");
        dao.persist(u);
        System.out.println("Usuario insertado desde servlet.");
    }
}