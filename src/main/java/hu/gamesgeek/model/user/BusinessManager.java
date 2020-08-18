package hu.gamesgeek.model.user;

import hu.gamesgeek.types.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessManager {

    @Autowired
    CrudManager crudManager;

    public UserDTO findUserDTOByUserId(String userId) {
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

    public User findUserById(String userId) {
        return crudManager.runSingleResultJPQL("SELECT user FROM " + User.class.getSimpleName() + " user" +
                " WHERE user.id = ?1", User.class, userId);
    }

    public <E extends BaseEntity> E save(E entity) {
        return crudManager.save(entity);
    }
}
