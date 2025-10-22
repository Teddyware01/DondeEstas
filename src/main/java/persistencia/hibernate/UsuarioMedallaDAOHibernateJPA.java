package persistencia.hibernate;

import dondeestas.Medalla;
import dondeestas.UsuarioMedalla;
import jakarta.persistence.EntityManager;
import persistencia.DAO.UsuarioMedallaDAO;
import persistencia.EMF;

import java.util.List;

public class UsuarioMedallaDAOHibernateJPA extends GenericDAOHibernateJPA<UsuarioMedalla>
        implements UsuarioMedallaDAO {


    public UsuarioMedallaDAOHibernateJPA() {
        super(UsuarioMedalla.class);
    }

    @Override
    public List<UsuarioMedalla> findByUsuario(Long idUsuario) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<UsuarioMedalla> resultado = null;

        try {
            resultado = em.createQuery(
                            "SELECT um FROM UsuarioMedalla um WHERE um.usuario.id = :idUsuario",
                            UsuarioMedalla.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }

    @Override
    public boolean usuarioTieneMedalla(Long idUsuario, Medalla tipoMedalla) {
        EntityManager em = EMF.getEMF().createEntityManager();
        boolean tieneMedalla = false;

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(um) FROM UsuarioMedalla um WHERE um.usuario.id = :idUsuario AND um.medalla = :tipo",
                            Long.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("tipo", tipoMedalla)
                    .getSingleResult();

            tieneMedalla = count != null && count > 0;
        } finally {
            em.close();
        }

        return tieneMedalla;
    }

    @Override
    public List<UsuarioMedalla> findByTipo(Medalla tipoMedalla) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<UsuarioMedalla> resultado = null;

        try {
            resultado = em.createQuery(
                            "SELECT um FROM UsuarioMedalla um WHERE um.medalla = :tipo",
                            UsuarioMedalla.class)
                    .setParameter("tipo", tipoMedalla)
                    .getResultList();
        } finally {
            em.close();
        }

        return resultado;
    }


}