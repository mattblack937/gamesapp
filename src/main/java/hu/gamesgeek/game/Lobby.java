package hu.gamesgeek.game;

import hu.gamesgeek.util.LobbyHandler;
import hu.gamesgeek.websocket.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class Lobby {

    public List<UserDTO> users = new ArrayList<>();

    public abstract int getNumberOfPlayers();

    public abstract GameType getGameType();

    public void join(UserDTO user){
        if (!users.contains(user)){
            users.add(user);
        }
    };

    public void startGame(){
        //TODO reflenction
        Game game = new Game(this);

        game.start();

        LobbyHandler.onStartGame(this);
    }

}
