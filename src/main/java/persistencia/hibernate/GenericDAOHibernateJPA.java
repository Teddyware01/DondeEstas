package persistencia.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import persistencia.DAO.GenericDAO;
import persistencia.EMF;

import java.util.List;

public class GenericDAOHibernateJPA<T>
        implements GenericDAO<T> {



    protected Class<T> persistentClass;
    protected EntityManager em;

    public GenericDAOHibernateJPA(Class<T> clase, EntityManager em) {
        this.persistentClass = clase;
        this.em=em;
    }
    public Class<T> getPersistentClass() {
        return persistentClass;

    }

    @Override
    public T persist(T entity) {
        em.persist(entity);
        return entity;
    }


    @Override
    public T update(T entity) {
        T entityMerged = null;
        entityMerged = em.merge(entity);
        return entityMerged;
    }

    @Override
    public void delete(T entity) {
            Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
            T attached = em.find(persistentClass, id);

            if (attached != null) {
                em.remove(attached);
            }

    }

    @Override
    public void delete(Long id) {
            T entity = em.find(persistentClass, id);
            if (entity != null){
                em.remove(entity);
            }
    }

    @Override
    public T get(Long id) {
        T entity = em.find(persistentClass, id);
        return entity;
    }

    @Override
    public List<T> getAll(String columnOrder) {
            Query consulta = em.createQuery("SELECT e FROM " +
                    getPersistentClass().getSimpleName() +
                    " e order by e." + columnOrder);

            return (List<T>) consulta.getResultList();
        }
}