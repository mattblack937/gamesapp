package hu.gamesgeek.util;

import hu.gamesgeek.websocket.dto.UserTokenDTO;
import hu.gamesgeek.websocket.messagehandler.StateMessageHandler;
import org.java_websocket.WebSocket;

import java.util.*;

public abstract class ConnectionHandler {

    private static Map<String, List<WebSocket>> userConnections = new HashMap<>();
    private static List<UserTokenDTO> userTokens = new ArrayList<>();

    public static Map<String, List<WebSocket>> getUserConnections() {
        return userConnections;
    }

    public static List<UserTokenDTO> getUserTokens() {
        return userTokens;
    }

    public static String getUserIdByWebSocket(WebSocket webSocket) {
        for (Map.Entry<String, List<WebSocket>> entry: userConnections.entrySet()) {
            if (entry.getValue().contains(webSocket)) {
                return entry.getKey();
            }
        }
        return null;
    }


    public static void addWebSocket(String userId, WebSocket webSocket) {
        if (userConnections.containsKey(userId)) {
            List<WebSocket> webSockets = new ArrayList<>();
            webSockets.add(webSocket);
            webSockets.addAll(userConnections.get(userId));
            userConnections.put(userId, webSockets);
        } else {
            userConnections.put(userId, Arrays.asList(webSocket));
        }
    }

    public static void removeUserToken(UserTokenDTO userToken) {
        userTokens.remove(userToken);
    }

    public static void removeWebSocket(WebSocket webSocket) {
        String userId = getUserIdByWebSocket(webSocket);
        if (userId != null) {
            removeWebSocket(userId, webSocket);
        }
    }

    private static void removeWebSocket(String userId, WebSocket webSocket) {
        webSocket.close();
        List<WebSocket> webSockets = new ArrayList<>();
        webSockets.addAll(userConnections.get(userId));
        webSockets.remove(webSocket);
        if (webSockets.isEmpty()){
            userConnections.remove(userId);
        } else {
            userConnections.put(userId, webSockets);
        }
    }

    public static List<WebSocket> getWebSockets() {
        List<WebSocket> result = new ArrayList<>();
        for(Map.Entry<String, List<WebSocket>> entry: userConnections.entrySet()){
            result.addAll(entry.getValue());
        }
        return result;
    }

    public static List<WebSocket> getWebSocketsByUserId(String userId) {
        return userConnections.get(userId);
    }

    public static Set<String> getAllUserIds() {
        return userConnections.keySet();
    }

    public static void addUserToken(UserTokenDTO userToken) {
        userTokens.add(userToken);
    }

    public static void onLogout(String userId) {
        List<WebSocket> webSockets = getWebSocketsByUserId(userId);
        if (webSockets != null){
            for (WebSocket webSocket: webSockets){
                webSocket.close();
            }
        }
        userConnections.remove(userId);
        StateMessageHandler.updateUserLists();
    }
}
