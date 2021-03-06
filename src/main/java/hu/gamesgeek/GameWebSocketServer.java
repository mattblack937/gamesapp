package hu.gamesgeek;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.types.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.types.dto.UserTokenDTO;
import hu.gamesgeek.websocket.messagehandler.AbstractMessageHandler;
import hu.gamesgeek.websocket.messagehandler.MessageHandler;
import hu.gamesgeek.websocket.messagehandler.StateMessageHandler;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.reflections.Reflections;

import java.io.IOException;
import java.net.InetSocketAddress;

public class GameWebSocketServer extends WebSocketServer {

    private static ObjectMapper mapper = new ObjectMapper();

    public GameWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            WSMessage wsMessage = mapper.readValue(message, WSMessage.class);
            Long userId = ConnectionHandler.getUserIdByWebSocket(webSocket);
            if (userId == null){
                if (wsMessage.getMessageType() == MessageType.USER_TOKEN){
                    boolean success = authenticate(mapper.readValue(wsMessage.getData(), UserTokenDTO.class), webSocket);
//                    if (success){
//                        StateMessageHandler.initializeAppState(webSocket);
//                        StateMessageHandler.updateUserLists();
//                    }
                } else {
                    webSocket.close();
                }
            } else {
                AbstractMessageHandler messageHandler = getMessageHandler(wsMessage.getMessageType());
                messageHandler.handleMessage(userId, mapper.readValue(wsMessage.getData(), messageHandler.getClass().getAnnotation(MessageHandler.class).dtoClass()));
            }
        } catch (Exception e) {
            System.out.println("Could not process WSMessage: "+message+", WebSocket: "+webSocket);
        }
    }

    private AbstractMessageHandler getMessageHandler(MessageType messageType) {
        Reflections ref = new Reflections("hu.gamesgeek.websocket.messagehandler");
        for (Class<?> clazz : ref.getTypesAnnotatedWith(MessageHandler.class)) {
            MessageHandler messageHandler = clazz.getAnnotation(MessageHandler.class);
            if (messageHandler.messageType().equals(messageType)){
                try {
                    return (AbstractMessageHandler) clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {

                }
            }
        }
        System.out.println("Could not create MessageHandler for MessageType: "+ messageType);
        return null;
    }


    private boolean authenticate(UserTokenDTO userTokenDTO, WebSocket webSocket) {
        UserTokenDTO found = ConnectionHandler.getUserTokens().stream().filter(ut -> ut.equals(userTokenDTO)).findFirst().orElse(null);
        if (found != null){
            ConnectionHandler.removeUserToken(found);
            ConnectionHandler.addWebSocket(found.getUserId(), webSocket);
            return true;
        }
        return false;
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        ConnectionHandler.removeAndCloseWebSocket(webSocket);
//        StateMessageHandler.updateUserLists();
    }

    @Override
    public void onError(WebSocket webSocket, Exception ex) {
        ConnectionHandler.removeAndCloseWebSocket(webSocket);
//        StateMessageHandler.updateUserLists();
    }

    public static void broadcastMessage(WSMessage message){
        try {
            String messageJson = mapper.writeValueAsString(message);
            for (WebSocket webSocket: ConnectionHandler.getWebSockets()) {
                webSocket.send(messageJson);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(WebSocket webSocket, WSMessage message) {
        try {
            String messageJson = mapper.writeValueAsString(message);
            webSocket.send(messageJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Long userId, WSMessage message) {
        try {
            String messageJson = mapper.writeValueAsString(message);
            for(WebSocket webSocket: ConnectionHandler.getWebSocketsByUserId(userId)){
                webSocket.send(messageJson);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
