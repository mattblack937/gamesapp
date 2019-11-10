package hu.gamesgeek.restful.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User findUserByUserName(String userName){
        return userDao.findUserByUserName(userName);
    }

}
