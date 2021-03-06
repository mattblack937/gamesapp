package hu.gamesgeek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import hu.gamesgeek.game.Game;
import hu.gamesgeek.game.Group;
import hu.gamesgeek.game.amoba.AmobaMoveDTO;
import hu.gamesgeek.model.user.User;
import hu.gamesgeek.types.GameType;
import hu.gamesgeek.model.user.BusinessManager;
import hu.gamesgeek.types.UserState;
import hu.gamesgeek.types.dto.ChatMessageDTO;
import hu.gamesgeek.util.*;
import hu.gamesgeek.types.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.types.dto.UserDTO;
import hu.gamesgeek.types.dto.UserTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class GameApi {

    @Autowired
    BusinessManager businessManager;

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping(path = "/invite/{userId}" )
    public void invite(@PathVariable Long userId, HttpServletRequest request) {
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
    public void login( @RequestBody UserNameAndPassword userNameAndPassword, HttpServletResponse response, HttpServletRequest request) {
        UserDTO oldUser = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        if (oldUser!=null){
            ConnectionHandler.removeAndCloseWebSockets(oldUser.getId());
        }

        if (userNameAndPassword.getUserName()==null || userNameAndPassword.getPassword()==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User foundUser = businessManager.findUserByUserName(userNameAndPassword.getUserName());

        if (foundUser == null || !userNameAndPassword.getPassword().equals(foundUser.getPassword())){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        session.setAttribute("user", new UserDTO(foundUser));
    }

    @PostMapping(path = "/create-account", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void createAccount(@RequestBody UserNameAndPassword userNameAndPassword, HttpServletResponse response, HttpServletRequest request) {
        UserDTO oldUser = getUserDTOFromRequest(request);

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        if (oldUser!=null){
            ConnectionHandler.removeAndCloseWebSockets(oldUser.getId());
        }

        if (userNameAndPassword.getUserName()==null || userNameAndPassword.getPassword()==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User foundUser = businessManager.findUserByUserName(userNameAndPassword.getUserName());

        if (foundUser != null){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        if (userNameAndPassword.getUserName().length()<3 || userNameAndPassword.getPassword().length()<3){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        User newUser = new User();
        newUser.setUserName(userNameAndPassword.getUserName());
        newUser.setPassword(userNameAndPassword.getPassword());
        businessManager.save(newUser);

        session.setAttribute("user", new UserDTO(newUser));
    }

    @PostMapping(path = "/remove-friend", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void removeFriend(@RequestBody Long id, HttpServletResponse response, HttpServletRequest request) {

        User user = businessManager.findUserById(getUserDTOFromRequest(request).getId());
        User friend = businessManager.findUserById(id);

        if(id == null || id.equals(user.getId()) || friend == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!user.getFriends().contains(friend)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        businessManager.dropFriendship(user, friend);

        List<User> toUpdate = new ArrayList<>();
        toUpdate.add(user);
        toUpdate.add(friend);
        ConnectionHandler.updateFriendList(toUpdate);
    }

    @PostMapping(path = "/friend-request", consumes = MediaType.APPLICATION_JSON_VALUE )
    public void friendRequest(@RequestBody String userName, HttpServletResponse response, HttpServletRequest request) {
        UserDTO userDTO = getUserDTOFromRequest(request);

        if(userName == null || userName.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (userName.equals(userDTO.getName())){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        User foundUser = businessManager.findUserByUserName(userName);
        if (foundUser == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        User currentUser = businessManager.findUserByUserName(userDTO.getName());
        if (currentUser.getFriends().contains(foundUser)){
            response.setStatus(HttpServletResponse.SC_FOUND);
            return;
        }

        if (currentUser.getFriendRequests().contains(foundUser)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        //OK...

        if (foundUser.getFriendRequests().contains(currentUser)){
            foundUser.getFriendRequests().remove(currentUser);
            businessManager.createFriendship(currentUser, foundUser);

            List<User> toUpdate = Lists.newArrayList();
            toUpdate.add(currentUser);
            toUpdate.add(foundUser);
            ConnectionHandler.updateFriendList(toUpdate);
            return;
        }

        currentUser.getFriendRequests().add(foundUser);
        businessManager.save(currentUser);
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
    public void chatMessage(@RequestBody String text, HttpServletRequest request) {
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

//    @GetMapping("/friends")
//    public String getFriends(HttpServletRequest request) {
//        User user = businessManager.findUserById(getUserDTOFromRequest(request).getId());
//
//        List<UserDTO> friends = Lists.newArrayList();
//        for (User friend: user.getFriends()){
//            UserDTO userDTO = new UserDTO(friend);
//            userDTO.setUserState(ConnectionHandler.getWebSocketsByUserId(friend.getId()).isEmpty() ? UserState.offline : UserState.online);
//            friends.add(userDTO);
//        }
//
//        try {
//            return mapper.writeValueAsString(friends);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


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
    public void acceptInvite(@PathVariable Long ownerId, HttpServletRequest request) {

        UserDTO user = getUserDTOFromRequest(request);
        UserDTO owner = ServiceHelper.getService(BusinessManager.class).findUserDTOByUserId(ownerId);

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
