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
import hu.gamesgeek.websocket.dto.LobbyDTO;
import hu.gamesgeek.websocket.dto.StateDTO;
import hu.gamesgeek.websocket.dto.UserDTO;
import org.java_websocket.WebSocket;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@GameLobby(gameType = GameType.AMOBA)
public class AmobaLobby extends Lobby {

    public AmobaLobby(){
    }

    @Override
    public GameType getGameType() {
        return GameType.AMOBA;
    }


    @Override
    public LobbyDTO toLobbyDTO() {
        LobbyDTO lobbyDTO = super.toLobbyDTO();

        return lobbyDTO;
    }
}
