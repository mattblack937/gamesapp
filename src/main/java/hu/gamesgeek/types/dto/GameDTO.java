package hu.gamesgeek.types.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hu.gamesgeek.types.GameType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDTO {
    private GameType gameType;
    private String data;

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
