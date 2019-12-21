package hu.gamesgeek.restful.user;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Repository
public class UserDao {

    private EntityManagerFactory entityManagerFactory;

    public UserDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory= entityManagerFactory;
    }

    public long createUser(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        if (user.getEnabled() == 0) {
            user.setEnabled((byte) 1);
        }
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user.getId();
    }

    public void deleteUser(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        User user = em.getReference(User.class, id);
        em.remove(user);
        em.getTransaction().commit();
        em.close();
    }

    public User findUser(long id)  {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    public User findUserByUserName(String userName)  {
        EntityManager em = entityManagerFactory.createEntityManager();
        User user = em.createQuery("SELECT user from " + User.class.getSimpleName() + " user" +
                " WHERE user.userName = :userName",
                User.class).setParameter("userName", userName).getSingleResult();
        em.close();
        return user;
    }

    public void updateUser(User newUser) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        User oldUser = em.find(User.class, newUser.getId());
        em.merge(newUser);
        em.getTransaction().commit();
        em.close();
    }

    public User findUserById(String userId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        User user = em.createQuery("SELECT user from " + User.class.getSimpleName() + " user" +
                        " WHERE user.id = :userId",
                User.class).setParameter("userId", Long.valueOf(userId)).getSingleResult();
        em.close();
        return user;
    }

    public User checkUserLogin(String userName, String password) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            User user = em.createQuery("SELECT user from " + User.class.getSimpleName() + " user" +
                            " WHERE user.userName = :userName AND user.password = :password ",
                    User.class).setParameter("userName", userName).setParameter("password", password).getSingleResult();
            em.close();
            return user;
        } catch (Exception e){
            return null;
        } finally {
            em.close();
        }
//        User user = em.createQuery("SELECT user from " + User.class.getSimpleName() + " user" +
//                        " WHERE user.userName = :userName AND user.password = :password ",
//                User.class).setParameter("userName", userName).setParameter("password", password).getSingleResult();
//        em.close();
//        return user;
    }
}
