package dondeestas;

import persistencia.DAO.FactoryDAO;
import persistencia.DAO.UsuarioDAO;
import dondeestas.Usuario;

public class TestHibernate {
    public static void main(String[] args) {
        UsuarioDAO dao = FactoryDAO.getUsuarioDAO();
        Usuario u = new Usuario(null, "Juan", "Perez", "juan@ejemplo.com",
                "1234", "1111", "Centro", "La Plata");
        dao.persist(u);
        System.out.println("Usuario insertado, tablas creadas.");
    }
}
