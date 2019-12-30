package hu.gamesgeek.model.user;

import hu.gamesgeek.types.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User findUserByUserName(String userName){
        return userDao.findUserByUserName(userName);
    }

    public UserDTO findUserDTOByUserId(String userId) {
        User user = userDao.findUserById(userId);
        UserDTO result = new UserDTO();
        result.setId(userId);
        result.setName(user.getUserName());
        return result;
    }

    public UserDTO checkUserLogin(String userName, String password) {
        User user = userDao.checkUserLogin(userName, password);
        return user==null ? null : new UserDTO(user);
    }
}
