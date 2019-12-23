package hu.gamesgeek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.game.GameType;
import hu.gamesgeek.game.Lobby;
import hu.gamesgeek.restful.user.UserService;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.util.LobbyHandler;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.websocket.dto.UserDTO;
import hu.gamesgeek.websocket.dto.UserTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.Serializable;
import java.util.UUID;

@RestController
@CrossOrigin
public class GameApi {

    @Autowired
    UserService userService;

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/userToken")
    public String getUserToken(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        UserTokenDTO userToken = new UserTokenDTO();
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setUserId(user.getId());

        ConnectionHandler.addUserToken(userToken);

        WSMessage wsMessage = new WSMessage();
        wsMessage.setMessageType(MessageType.USER_TOKEN);
        try {
            wsMessage.setData(mapper.writeValueAsString(userToken.getToken()));
            return mapper.writeValueAsString(wsMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void login( @RequestBody UserNameAndPassword userNameAndPassword, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        if (user != null){
            ConnectionHandler.getWebSocketsByUserId(user.getId()).forEach(webSocket -> ConnectionHandler.removeWebSocket(webSocket));
        }

        if (userNameAndPassword.getUserName()==null || userNameAndPassword.getPassword()==null){
            //FIXME ERROR HANDLING
            return;
        }

        session.setAttribute("user", userService.checkUserLogin(userNameAndPassword.getUserName(), userNameAndPassword.getPassword() ) ) ;
    }

    @GetMapping(path = "/logout" )
    public void logout( HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        request.getSession(true);

        if (user != null){
            ConnectionHandler.getWebSocketsByUserId(user.getId()).forEach(webSocket -> ConnectionHandler.removeWebSocket(webSocket));
        }
    }

    private UserDTO getUserDTOFromRequest(HttpServletRequest request) {
        return (UserDTO) request.getSession().getAttribute("user");
    }

    @GetMapping("/create-lobby/{gameType}")
    public void createLobby(@PathVariable("gameType") String gameType, HttpServletRequest request) {
        Lobby lobby = LobbyHandler.createLobby(GameType.valueOf(gameType));
    }

    @GetMapping("/user")
    public String getUser(HttpServletRequest request) {

        UserDTO user = getUserDTOFromRequest(request);

        if (user == null){
            return null;
        }

        try {
            return mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }



    private static class UserNameAndPassword implements Serializable {
        String userName;
        String password;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
