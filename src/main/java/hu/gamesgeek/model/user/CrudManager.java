package hu.gamesgeek.model.user;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CrudManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public <E extends BaseEntity> E save(E baseEntity) {
        if (baseEntity.getId() == null){
            entityManager.persist(baseEntity);
        } else {
            entityManager.merge(baseEntity);
        }
        return baseEntity;
    }

    @Transactional
    public <E extends BaseEntity> void delete(E baseEntity) {
        entityManager.remove(baseEntity);
    }

    @Transactional
    public <E extends BaseEntity> E find(Class<E> clazz, Long id)  {
        return entityManager.find(clazz, id);
    }

    public  <E extends BaseEntity> List<E> runJPQL(String jpql, Class<E> clazz, Object... params) {
        Query query = entityManager.createQuery(jpql, clazz);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i+1, params[i]);
        }
        return query.getResultList();
    }

    public <E extends BaseEntity>  E runSingleResultJPQL(String jpql, Class<E> clazz, Object... params) {
        Query query = entityManager.createQuery(jpql, clazz);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i+1, params[i]);
        }
        try {
            return (E) query.getSingleResult();
        } catch (NoResultException noResultException){
            return null;
        }
    }

}
