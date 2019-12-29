package hu.gamesgeek.websocket.messagehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.WSServer;
import hu.gamesgeek.game.Lobby;
import hu.gamesgeek.restful.user.UserService;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.util.LobbyHandler;
import hu.gamesgeek.util.ServiceHelper;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.websocket.dto.LobbyDTO;
import hu.gamesgeek.websocket.dto.StateDTO;
import hu.gamesgeek.websocket.dto.UserDTO;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@MessageHandler(messageType = MessageType.STATE, dtoClass = StateDTO.class)
public class StateMessageHandler extends AbstractMessageHandler<StateDTO> {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void initializeAppState(WebSocket webSocket) throws JsonProcessingException {
        WSMessage wsMessage = new WSMessage();
        wsMessage.setMessageType(MessageType.STATE);
        StateDTO stateDTO = new StateDTO();

        Lobby lobby = LobbyHandler.getLobbyByUserId(ConnectionHandler.getUserIdByWebSocket(webSocket));
        if (lobby != null){
            LobbyDTO lobbyDTO = lobby.toLobbyDTO();
            stateDTO.setLobby(lobbyDTO);
        }

        wsMessage.setData(mapper.writeValueAsString(stateDTO));
        WSServer.sendMessage(webSocket, wsMessage);
    }

    @Override
    public void handleMessage(String userId, StateDTO wsMessage) {

    }

    @Override
    public MessageType getMessageType() {
        return MessageType.STATE;
    }

    @Override
    public Class getDtoClass() {
        return StateDTO.class;
    }

    public static void updateUserLists(){
        UserService userService = ServiceHelper.getService(UserService.class);
        Set<String> userIds = ConnectionHandler.getAllUserIds();
        List<UserDTO> users = new ArrayList<>();
        for(String userId: userIds){
            UserDTO userDTO = userService.findUserDTOByUserId(userId);
            users.add(userDTO);
        }
        StateDTO stateDTO = new StateDTO();
        stateDTO.setUsers(users);

        WSMessage wsMessage = new WSMessage();
        String data = null;
        try {
            data = mapper.writeValueAsString(stateDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        wsMessage.setData(data);
        wsMessage.setMessageType(MessageType.STATE);
        WSServer.broadcastMessage(wsMessage);
    }

}
