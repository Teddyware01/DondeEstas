package persistencia.DAO;

import jakarta.persistence.EntityManager;
import persistencia.hibernate.*;

public class FactoryDAO {
    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOHibernateJPA();
    }
    public static AvistamientoDAO getAvistamientoDAO() {
        return new AvistamientoDAOHibernateJPA();
    }
    public static EstadoDAO getEstadoDAO() {
        return new EstadoDAOHibernateJPA();
    }
    public static MascotaDAO getMascotaDAO(EntityManager em) {
        return new MascotaDAOHibernateJPA(em);
    }
    public static MedallaDAO getMedallaDAO() {
        return new MedallaDAOHibernateJPA();

    }
    public static PuntajeDAO getPuntajeDAO() {
        return new PuntajeDAOHibernateJPA();
    }
    public static UbicacionDAO getUbicacionDAO() {
        return new UbicacionDAOHibernateJPA();
    }
    public static UsuarioMedallaDAO getUsuarioMedallaDAO() {
        return new UsuarioMedallaDAOHibernateJPA();
    }
    public static UsuarioPuntajeDAO getUsuarioPuntajeDAO() {
        return new UsuarioPuntajeDAOHibernateJPA();
    }

}