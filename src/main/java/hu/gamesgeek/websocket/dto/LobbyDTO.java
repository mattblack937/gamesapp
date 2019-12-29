package hu.gamesgeek.websocket.dto;

import hu.gamesgeek.game.GameType;

import java.util.ArrayList;
import java.util.List;

public class LobbyDTO {

    private GameType gameType;
    private List<UserDTO> players = new ArrayList<>();
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public List<UserDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserDTO> players) {
        this.players = players;
    }
}
