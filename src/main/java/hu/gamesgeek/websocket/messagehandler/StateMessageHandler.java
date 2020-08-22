package hu.gamesgeek.websocket.messagehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.GameWebSocketServer;
import hu.gamesgeek.game.Game;
import hu.gamesgeek.game.Group;
import hu.gamesgeek.model.user.BusinessManager;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.util.GameHandler;
import hu.gamesgeek.util.GroupHandler;
import hu.gamesgeek.util.ServiceHelper;
import hu.gamesgeek.types.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.types.dto.StateDTO;
import hu.gamesgeek.types.dto.UserDTO;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@MessageHandler(messageType = MessageType.STATE, dtoClass = StateDTO.class)
public class StateMessageHandler extends AbstractMessageHandler<StateDTO> {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void initializeAppState(WebSocket webSocket) throws JsonProcessingException {
        UserDTO user = ServiceHelper.getService(BusinessManager.class).findUserDTOByUserId(ConnectionHandler.getUserIdByWebSocket(webSocket));

        WSMessage wsMessage = new WSMessage();
        wsMessage.setMessageType(MessageType.STATE);
        StateDTO stateDTO = new StateDTO();

        Group group = GroupHandler.getGroupOfUser(user);
        if (group != null){
            stateDTO.setGroup(group.toGroupDTO());
        }

        Game game = GameHandler.getGameOfUser(user);
        if (game != null){
            stateDTO.setGame(game.toGameDTO());
        }

        wsMessage.setData(mapper.writeValueAsString(stateDTO));
        GameWebSocketServer.sendMessage(webSocket, wsMessage);
    }

    @Override
    public void handleMessage(Long userId, StateDTO wsMessage) {

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
        BusinessManager businessManager = ServiceHelper.getService(BusinessManager.class);
        Set<Long> userIds = ConnectionHandler.getAllUserIds();
        List<UserDTO> users = new ArrayList<>();
        for(Long userId: userIds){
            UserDTO userDTO = businessManager.findUserDTOByUserId(userId);
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
        GameWebSocketServer.broadcastMessage(wsMessage);
    }

}
