package hu.gamesgeek.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.WSServer;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.util.LobbyHandler;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.websocket.dto.LobbyDTO;
import hu.gamesgeek.websocket.dto.StateDTO;
import hu.gamesgeek.websocket.dto.UserDTO;
import org.apache.tomcat.websocket.WsContainerProvider;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Lobby {

    protected ObjectMapper objectMapper = new ObjectMapper();

    private String id = UUID.randomUUID().toString();

    public abstract GameType getGameType();

    public void startGame(){
        //TODO reflenction
        Game game = new Game(this);

        game.start();

        LobbyHandler.onStartGame(this);
    }

    public void updateUsers(){
        WSMessage message = toWsMessage();
        for(String userId: LobbyHandler.getUserIdsByLobby(this)){
            for (WebSocket webSocket: ConnectionHandler.getWebSocketsByUserId(userId)){
                WSServer.sendMessage(webSocket, message);
            }
        }
    };

    public WSMessage toWsMessage(){
        WSMessage message = new WSMessage();
        message.setMessageType(MessageType.STATE);
        StateDTO stateDTO = new StateDTO();

        stateDTO.setLobby(toLobbyDTO());

        try {
            message.setData(objectMapper.writeValueAsString(stateDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return message;
    }


    public LobbyDTO toLobbyDTO() {
        LobbyDTO lobbyDTO = new LobbyDTO();
        lobbyDTO.setId(id);
        lobbyDTO.setGameType(getGameType());
        lobbyDTO.setPlayers(LobbyHandler.getPlayersOfLobby(this));
        return lobbyDTO;
    }
}
