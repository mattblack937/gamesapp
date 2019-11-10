package hu.gamesgeek.websocket;


import hu.gamesgeek.authenticate.UserToken;
import hu.gamesgeek.dto.ChatMessageDTO;
import hu.gamesgeek.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

public class ChatServer extends WebSocketServer {

    private ObjectMapper mapper = new ObjectMapper();

    private Map<UserDTO, List<WebSocket>> userConnections = new HashMap<>();
    private List<UserToken> userTokens = Collections.synchronizedList(new ArrayList<>());

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        UserDTO user = getUserByWebSocket(webSocket);
        if (user != null) {
            userConnections.get(user).remove(webSocket);
            updateUserLists();
        }
    }

    private UserDTO getUserByWebSocket(WebSocket webSocket) {
        UserDTO user = null;
        for (Map.Entry<UserDTO, List<WebSocket>> entry: userConnections.entrySet()) {
            if (entry.getValue().contains(webSocket)) {
                user = entry.getKey();
            }
        }
        return user;
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            Message msg = mapper.readValue(message, Message.class);
            if (getUserByWebSocket(webSocket) == null){
                if (msg.getType() == MessageType.AUTHENTICATE){
//                    UserToken userToken = mapper.readValue(msg.getData(), UserToken.class);
                    authenticate(msg.getUser(), mapper.readValue(msg.getData(), UserToken.class).getToken(), webSocket);
                }
            } else{
                switch (msg.getType()) {
                    case CHAT_MESSAGE:
                        sendChatMessage(msg.getUser(), mapper.readValue(msg.getData(), ChatMessageDTO.class));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendChatMessage(UserDTO user, ChatMessageDTO chatMessage) {
        chatMessage.setUser(user);
        Message message = new Message();
        String data = null;
        try {
            data = mapper.writeValueAsString(chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        message.setData(data);
        message.setType(MessageType.CHAT_MESSAGE);
        broadcastMessage(message);
    }

    private void authenticate(UserDTO user, String token, WebSocket webSocket) {
        UserToken userToken = userTokens.stream().filter(ut -> user.getId().equals(ut.getUserId()) && token.equals(ut.getToken())).findFirst().orElse(null);
        if (userToken != null){
            userTokens.remove(userToken);

            List<WebSocket> webSocketList = new ArrayList<>();

            if (userConnections.containsKey(user)) {
                webSocketList.addAll(userConnections.get(user));
            }
            webSocketList.add(webSocket);
            userConnections.put(user, webSocketList);

            updateUserLists();
        }
    }

    private void updateUserLists() {
        List<UserDTO> users = new ArrayList<>(userConnections.keySet());
        Message message = new Message();
        String data = null;
        try {
            data = mapper.writeValueAsString(users);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        message.setData(data);
        message.setType(MessageType.USER_LIST);
        broadcastMessage(message);
    }

    @Override
    public void onError(WebSocket webSocket, Exception ex) {
        UserDTO user = getUserByWebSocket(webSocket);
        if (user != null) {
            userConnections.get(user).remove(webSocket);
            updateUserLists();
        }
    }

    public void broadcastMessage(Message message){
        try {
            String messageJson = mapper.writeValueAsString(message);
            for (Map.Entry<UserDTO, List<WebSocket>> entry: userConnections.entrySet()) {
                for (WebSocket webSocket: entry.getValue()) {
                    webSocket.send(messageJson);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(WebSocket conn, Message message) {
        try {
            String messageJson = mapper.writeValueAsString(message);
            conn.send(messageJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<UserToken> getUserTokens() {
        return userTokens;
    }

}
