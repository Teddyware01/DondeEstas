package persistencia.DAO;

import persistencia.hibernate.UsuarioDAOHibernateJPA;

public class FactoryDAO {
    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOHibernateJPA();
    }

}