package hu.gamesgeek.game.amoba;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.gamesgeek.WSServer;
import hu.gamesgeek.game.GameLobby;
import hu.gamesgeek.game.Lobby;
import hu.gamesgeek.game.GameType;
import hu.gamesgeek.restful.user.UserService;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.util.ServiceHelper;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.websocket.dto.UserDTO;
import org.java_websocket.WebSocket;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@GameLobby(gameType = GameType.AMOBA)
public class AmobaLobby extends Lobby {

    public AmobaLobby(){
        String a = "a";
    }

    @Override
    public int getNumberOfPlayers() {
        return 2;
    }

    @Override
    public GameType getGameType() {
        return GameType.AMOBA;
    }

    @Override
    public WSMessage toWsMessage() {
        WSMessage message = new WSMessage();
        message.setMessageType(MessageType.LOBBY);
        AmobaLobbyDTO amobaLobbyDTO = new AmobaLobbyDTO();
        amobaLobbyDTO.setUsers(usersIds.stream().map(usersId-> ServiceHelper.getService(UserService.class).findUserDTOByUserId(usersId)).collect(Collectors.toList()));
        try {
            message.setData(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return message;
    }

    private static class AmobaLobbyDTO implements Serializable {
        private List<UserDTO> users;

        public List<UserDTO> getUsers() {
            return users;
        }

        public void setUsers(List<UserDTO> users) {
            this.users = users;
        }
    }

}
