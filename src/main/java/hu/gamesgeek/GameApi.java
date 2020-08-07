package hu.gamesgeek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.game.Game;
import hu.gamesgeek.game.Group;
import hu.gamesgeek.game.amoba.AmobaMoveDTO;
import hu.gamesgeek.types.GameType;
import hu.gamesgeek.model.user.UserService;
import hu.gamesgeek.types.dto.ChatMessageDTO;
import hu.gamesgeek.util.*;
import hu.gamesgeek.types.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.types.dto.UserDTO;
import hu.gamesgeek.types.dto.UserTokenDTO;
import hu.gamesgeek.websocket.messagehandler.ChatMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

@RestController
@CrossOrigin
public class GameApi {

    @Autowired
    UserService userService;

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping(path = "/invite/{userId}" )
    public void invite(@PathVariable String userId, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        if (userId == null){
            return;
        }

        if (user.getId().equals(userId)){
            return;
        }

        GroupHandler.sendInvite(user, userId);
    }

    @GetMapping("/userToken")
    public String getUserToken(HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest( request);

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
        UserDTO oldUser = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        if (userNameAndPassword.getUserName()==null || userNameAndPassword.getPassword()==null){
            //FIXME ERROR HANDLING
            return;
        }

        session.setAttribute("user", userService.checkUserLogin(userNameAndPassword.getUserName(), userNameAndPassword.getPassword() ) ) ;

        if (oldUser!=null && oldUser.getId()!=null){
            ConnectionHandler.removeAndCloseWebSockets(oldUser.getId());
        }
    }



    @PostMapping(path = "/move", consumes = MediaType.APPLICATION_JSON_VALUE  )
    public void move( @RequestBody String  moveJSON, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);
        Game game = GameHandler.getGameOfUser(user);

        if (game == null){
            return;
        }

        Object move = null;
        switch (game.getGameType()){
            case AMOBA:
                try {
                    move = mapper.readValue(moveJSON, AmobaMoveDTO.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        if (game.legal(user, move)){
            game.move(user, move);
            GameHandler.updateUsers(game);
        }
    }

    @PostMapping(path = "/chat-message", consumes = MediaType.APPLICATION_JSON_VALUE  )
    public void chatMessage( @RequestBody String text, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        if(!StringUtils.isEmpty(text)){
            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setMessage(text);
            chatMessageDTO.setUser(user);

            WSMessage wsMessage = new WSMessage();
            String data = null;
            try {
                data = mapper.writeValueAsString(chatMessageDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            wsMessage.setData(data);
            wsMessage.setMessageType(MessageType.CHAT_MESSAGE);

            GameWebSocketServer.broadcastMessage(wsMessage);
        }
    }

    @PostMapping(path = "/post1", consumes = MediaType.APPLICATION_JSON_VALUE  )
    public void post1( @RequestBody UserNameAndPassword userNameAndPassword, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);
    }

    @PostMapping(path = "/post2", consumes = MediaType.APPLICATION_JSON_VALUE  )
    public void post2( @RequestBody UserNameAndPassword userNameAndPassword, HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);
    }

    @GetMapping(path = "/logout" )
    public void logout( HttpServletRequest request) {
        UserDTO user = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        request.getSession(true);

        if (user!=null && user.getId()!=null){
            ConnectionHandler.removeAndCloseWebSockets(user.getId());
        }
    }


    private UserDTO getUserDTOFromRequest(HttpServletRequest request) {
        return (UserDTO) request.getSession().getAttribute("user");
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


    @GetMapping("/get1")
    public String get1(HttpServletRequest request) {

        UserDTO user = getUserDTOFromRequest(request);

        return "1";
    }

    @GetMapping("/get2")
    public String get2(HttpServletRequest request) {

        UserDTO user = getUserDTOFromRequest(request);
        return "2";

    }

    @GetMapping("/leave-group")
    public void leaveGroup(HttpServletRequest request) {

        UserDTO user = getUserDTOFromRequest(request);

        if (GroupHandler.getGroupOfUser(user) == null){
            return;
        }

        GroupHandler.leaveGroup(user);
    }


    @PostMapping(path = "/start-game/{gameType}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void startGame(@PathVariable GameType gameType, @RequestBody String gameSettingsJSON, HttpServletRequest request) {

        UserDTO user = getUserDTOFromRequest(request);
        Group group = GroupHandler.getGroupOfUser(user);

        if (group == null || !group.getOwner().equals(user)){
            return;
        }

        Object settings = null;
        if (gameSettingsJSON != null){
            settings = GameHandler.readSettingsFromJSON(gameType, gameSettingsJSON);
        }

        if (!GameHandler.canStartGame(group, gameType, settings)){
            return;
        }

        GameHandler.startGame(group, gameType, settings);
    }

    @GetMapping("/accept-invite/{ownerId}")
    public void acceptInvite(@PathVariable String ownerId, HttpServletRequest request) {

        UserDTO user = getUserDTOFromRequest(request);
        UserDTO owner = ServiceHelper.getService(UserService.class).findUserDTOByUserId(ownerId);

        Group groupOfUser = GroupHandler.getGroupOfUser(user);
        Group groupOfOwner = GroupHandler.getGroupOfUser(owner);

        if (groupOfOwner == groupOfUser && groupOfOwner !=null){
            return;
        }

        if (groupOfOwner == null){
            groupOfOwner = GroupHandler.createGroup(owner);
        }

        if (groupOfUser == null){
            groupOfOwner.joinUser(user);
        } else {
            groupOfOwner.joinGroup(groupOfUser);
        }

        GroupHandler.updateGroup(groupOfOwner);
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
