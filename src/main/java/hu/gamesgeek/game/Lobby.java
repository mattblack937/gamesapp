package hu.gamesgeek.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.WSServer;
import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.util.LobbyHandler;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.websocket.dto.UserDTO;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;

public abstract class Lobby {

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected List<String> usersIds = new ArrayList<>();

    public abstract int getNumberOfPlayers();

    public abstract GameType getGameType();

    public void join(String userId){
        usersIds.add(userId);
    }

    public boolean containsUserId(String userId){
        return usersIds.contains(userId);
    }

    public void startGame(){
        //TODO reflenction
        Game game = new Game(this);

        game.start();

        LobbyHandler.onStartGame(this);
    }

    public List<String > getUserIds(){
        return usersIds;
    }

    public void updateUsers(){
        for(String userId: usersIds){
            for (WebSocket webSocket: ConnectionHandler.getWebSocketsByUserId(userId)){
                WSServer.sendMessage(webSocket, toWsMessage());
            }
        }
    };

    public abstract WSMessage toWsMessage();
}
