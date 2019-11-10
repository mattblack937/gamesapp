package hu.gamesgeek.restful.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.JpaSpringApplication;
import hu.gamesgeek.authenticate.UserTokenRemover;
import hu.gamesgeek.dto.UserDTO;
import hu.gamesgeek.websocket.Message;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.authenticate.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/userToken")
    public String userToken(Authentication authentication) {
        if (authentication != null) {
            User user = userService.findUserByUserName(authentication.getName());

            List<UserToken> userTokens = JpaSpringApplication.chatServer.getUserTokens();

            UserToken userToken = new UserToken();
            userToken.setToken(UUID.randomUUID().toString());
            userToken.setUserId(String.valueOf(user.getId()));

            userTokens.add(userToken);

            UserTokenRemover userTokenRemover = new UserTokenRemover(userToken);
            userTokenRemover.start();

            Message message = new Message();
            message.setType(MessageType.USER_TOKEN);
            try {
                message.setData(mapper.writeValueAsString(userToken.getToken()));
                return mapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @GetMapping("/user")
    public String getUser(Authentication authentication) {
        if (authentication != null) {
            try {
                User user = userService.findUserByUserName(authentication.getName());
                UserDTO userDTO = new UserDTO();
                userDTO.setName(authentication.getName());
                userDTO.setId(String.valueOf(user.getId()));
                return mapper.writeValueAsString(userDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
