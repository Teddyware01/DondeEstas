package persistencia.DAO;

import jakarta.persistence.EntityManager;
import persistencia.hibernate.*;

public class FactoryDAO {
    public static UsuarioDAO getUsuarioDAO(EntityManager em) {
        return new UsuarioDAOHibernateJPA(em);
    }
    public static AvistamientoDAO getAvistamientoDAO(EntityManager em) {
        return new AvistamientoDAOHibernateJPA(em);
    }
    public static EstadoDAO getEstadoDAO(EntityManager em) {
        return new EstadoDAOHibernateJPA(em);
    }
    public static MascotaDAO getMascotaDAO(EntityManager em) {
        return new MascotaDAOHibernateJPA(em);
    }
    public static MedallaDAO getMedallaDAO(EntityManager em) {
        return new MedallaDAOHibernateJPA(em);

    }
    public static PuntajeDAO getPuntajeDAO(EntityManager em) {
        return new PuntajeDAOHibernateJPA(em);
    }
    public static UbicacionDAO getUbicacionDAO(EntityManager em) {
        return new UbicacionDAOHibernateJPA(em);
    }
    public static UsuarioMedallaDAO getUsuarioMedallaDAO(EntityManager em) {
        return new UsuarioMedallaDAOHibernateJPA(em);
    }
    public static UsuarioPuntajeDAO getUsuarioPuntajeDAO(EntityManager em) {
        return new UsuarioPuntajeDAOHibernateJPA(em);
    }
}