package hu.gamesgeek.model.user;

import hu.gamesgeek.types.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BusinessManager {

    @Autowired
    CrudManager crudManager;

    public UserDTO findUserDTOByUserId(Long userId) {
        User user = findUserById(userId);
        UserDTO result = new UserDTO();
        result.setId(userId);
        result.setName(user.getUserName());
        return result;
    }

    public UserDTO checkUserLogin(String userName, String password) {
        User user = findUserByUserName(userName);
        if(user == null || password == null || !password.equals(user.getPassword())){
            return null;
        }
        return new UserDTO(user);
    }

    public User findUserByUserName(String userName)  {
        return crudManager.runSingleResultJPQL("SELECT user FROM " + User.class.getSimpleName() + " user" +
                " WHERE user.userName = ?1", User.class, userName);
    }

    public User findUserById(Long id) {
        return crudManager.runSingleResultJPQL("SELECT user FROM " + User.class.getSimpleName() + " user" +
                " WHERE user.id = ?1", User.class, id);
    }

    public User findEagerUserById(Long id) {
        return crudManager.runSingleResultJPQL("SELECT user FROM " + User.class.getSimpleName() + " user" +
                " LEFT JOIN FETCH user.friends " +
                " WHERE user.id = ?1", User.class, id);
    }

    public <E extends BaseEntity> E save(E entity) {
        return crudManager.save(entity);
    }

    public <E extends BaseEntity> Collection<E> save(Collection<E> entities) {
        entities.forEach(e-> save(e));
        return entities;
    }

    public <E extends BaseEntity> E[] save(E ...entities) {
        for(E entity: entities){
            save(entity);
        }
        return entities;
    }

    public void createFriendship(Long userId1, Long userId2){
        createFriendship(findUserById(userId1), findUserById(userId2));
    }


    public void createFriendship(String userName1, String userName2){
        createFriendship(findUserByUserName(userName1), findUserByUserName(userName2));
    }

    public void createFriendship(User user1, User user2){
        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
        save(user1, user2);
    }

    public void dropFriendship(Long userId1, Long userId2){
        dropFriendship(findUserById(userId1), findUserById(userId2));
    }

    public void dropFriendship(String userName1, String userName2){
        dropFriendship(findUserByUserName(userName1), findUserByUserName(userName2));
    }

    public void dropFriendship(User user1, User user2){
        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);
        save(user1, user2);
    }


}
