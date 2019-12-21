package hu.gamesgeek.websocket.messagehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.GameSpringApplication;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.util.ServiceHelper;
import hu.gamesgeek.restful.user.UserService;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.websocket.dto.StateDTO;
import hu.gamesgeek.websocket.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@MessageHandler(messageType = MessageType.STATE, dtoClass = StateDTO.class)
public class StateMessageHandler extends AbstractMessageHandler<StateDTO> {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handleMessage(String userId, StateDTO wsMessage) {

    }

    public static void updateUserList(){
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
        GameSpringApplication.getWsServer().broadcastMessage(wsMessage);
    }

}
