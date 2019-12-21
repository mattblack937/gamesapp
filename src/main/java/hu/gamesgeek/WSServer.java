package hu.gamesgeek;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.websocket.dto.UserTokenDTO;
import hu.gamesgeek.websocket.messagehandler.AbstractMessageHandler;
import hu.gamesgeek.websocket.messagehandler.MessageHandler;
import hu.gamesgeek.websocket.messagehandler.StateMessageHandler;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.reflections.Reflections;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WSServer extends WebSocketServer {

    private ObjectMapper mapper = new ObjectMapper();

    public WSServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            WSMessage wsMessage = mapper.readValue(message, WSMessage.class);
            String userId = ConnectionHandler.getUserIdByWebSocket(webSocket);
            if (userId == null){
                if (wsMessage.getMessageType() == MessageType.USER_TOKEN){
                    authenticate(mapper.readValue(wsMessage.getData(), UserTokenDTO.class), webSocket);
                    StateMessageHandler.updateUserList();
                }
            } else {
                AbstractMessageHandler messageHandler = getMessageHandler(wsMessage.getMessageType());
                messageHandler.handleMessage(userId, mapper.readValue(wsMessage.getData(), messageHandler.getClass().getAnnotation(MessageHandler.class).dtoClass()));
            }
        } catch (IOException e) {
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


    private void authenticate(UserTokenDTO userTokenDTO, WebSocket webSocket) {
        UserTokenDTO found = ConnectionHandler.getUserTokens().stream().filter(ut -> ut.equals(userTokenDTO)).findFirst().orElse(null);
        if (found != null){
            ConnectionHandler.removeUserToken(found);
            ConnectionHandler.addWebSocket(found.getUserId(), webSocket);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        ConnectionHandler.removeWebSocket(webSocket);
        StateMessageHandler.updateUserList();
    }

    @Override
    public void onError(WebSocket webSocket, Exception ex) {
        ConnectionHandler.removeWebSocket(webSocket);
        StateMessageHandler.updateUserList();
    }

    public void broadcastMessage(WSMessage message){
        try {
            String messageJson = mapper.writeValueAsString(message);
            for (WebSocket webSocket: ConnectionHandler.getWebSockets()) {
                webSocket.send(messageJson);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(WebSocket webSocket, WSMessage message) {
        try {
            String messageJson = mapper.writeValueAsString(message);
            webSocket.send(messageJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String userId, WSMessage message) {
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
