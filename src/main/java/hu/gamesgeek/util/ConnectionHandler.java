package hu.gamesgeek.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import hu.gamesgeek.GameWebSocketServer;
import hu.gamesgeek.model.user.BusinessManager;
import hu.gamesgeek.model.user.User;
import hu.gamesgeek.types.MessageType;
import hu.gamesgeek.types.UserState;
import hu.gamesgeek.types.dto.UserDTO;
import hu.gamesgeek.types.dto.UserTokenDTO;
import hu.gamesgeek.websocket.WSMessage;
import org.java_websocket.WebSocket;

import java.util.*;

public abstract class ConnectionHandler {

    private static ObjectMapper mapper = new ObjectMapper();

    private static Map<Long, List<WebSocket>> userConnections = new HashMap<>();
    private static List<UserTokenDTO> userTokens = new ArrayList<>();

    public static Map<Long, List<WebSocket>> getUserConnections() {
        return userConnections;
    }

    public static List<UserTokenDTO> getUserTokens() {
        return userTokens;
    }

    public static Long getUserIdByWebSocket(WebSocket webSocket) {
        for (Map.Entry<Long, List<WebSocket>> entry: userConnections.entrySet()) {
            if (entry.getValue().contains(webSocket)) {
                return entry.getKey();
            }
        }
        return null;
    }


    public static void addWebSocket(Long userId, WebSocket webSocket) {
        if (userConnections.containsKey(userId)) {
            List<WebSocket> webSockets = new ArrayList<>();
            webSockets.add(webSocket);
            webSockets.addAll(userConnections.get(userId));
            userConnections.put(userId, webSockets);
        } else {
            userConnections.put(userId, Arrays.asList(webSocket));

            List<User> toUpdate = Lists.newArrayList();
            User user = ServiceHelper.getService(BusinessManager.class).findEagerUserById(userId);
            toUpdate.add(user);
            toUpdate.addAll(user.getFriends());

            updateFriendList(toUpdate);
        }
    }

    public static void updateFriendList(List<User> userList) {
        userList.forEach(user -> updateFriendList(user));
    }

    private static void updateFriendList(User user) {
        user = ServiceHelper.getService(BusinessManager.class).findEagerUserById(user.getId());

        List<UserDTO> friends = Lists.newArrayList();
        for (User friend: user.getFriends()){
            UserDTO userDTO = new UserDTO(friend);
            userDTO.setUserState(ConnectionHandler.getWebSocketsByUserId(friend.getId()).isEmpty() ? UserState.offline : UserState.online);
            friends.add(userDTO);
        }

        try {
            mapper.writeValueAsString(friends);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        WSMessage wsMessage = new WSMessage();
        wsMessage.setMessageType(MessageType.FRIEND_LIST);
        try {
            wsMessage.setData(mapper.writeValueAsString(friends));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        GameWebSocketServer.sendMessage(user.getId(), wsMessage);
    }

    public static void removeUserToken(UserTokenDTO userToken) {
        userTokens.remove(userToken);
    }

    public static void removeAndCloseWebSockets(Long userId) {
        userConnections.get(userId).stream().forEach(webSocket -> removeWebSocket(userId, webSocket));
    }

    public static void removeAndCloseWebSocket(WebSocket webSocket) {
        Long userId = getUserIdByWebSocket(webSocket);
        if (userId != null) {
            removeWebSocket(userId, webSocket);
        }
        webSocket.close();
    }

    private static void removeWebSocket(Long userId, WebSocket webSocket) {
        List<WebSocket> webSockets = new ArrayList<>();
        webSockets.addAll(userConnections.get(userId));
        webSockets.remove(webSocket);
        if (webSockets.isEmpty()){
            userConnections.remove(userId);

            User user = ServiceHelper.getService(BusinessManager.class).findEagerUserById(userId);
            updateFriendList(new ArrayList<>(user.getFriends()));
        } else {
            userConnections.put(userId, webSockets);
        }
    }

    public static List<WebSocket> getWebSockets() {
        List<WebSocket> result = new ArrayList<>();
        for(Map.Entry<Long, List<WebSocket>> entry: userConnections.entrySet()){
            result.addAll(entry.getValue());
        }
        return result;
    }

    public static List<WebSocket> getWebSocketsByUserId(Long userId) {
        return userConnections.containsKey(userId) ? userConnections.get(userId) : new ArrayList<>();
    }

    public static Set<Long> getAllUserIds() {
        return userConnections.keySet();
    }

    public static void addUserToken(UserTokenDTO userToken) {
        userTokens.add(userToken);
    }

//    public static void onLogout(String userId) {
//        List<WebSocket> webSockets = getWebSocketsByUserId(userId);
//        if (webSockets != null){
//            for (WebSocket webSocket: webSockets){
//                webSocket.close();
//            }
//        }
//        userConnections.remove(userId);
//        StateMessageHandler.updateUserLists();
//    }
}
