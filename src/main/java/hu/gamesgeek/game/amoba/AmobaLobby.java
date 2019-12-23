package hu.gamesgeek.game.amoba;

import hu.gamesgeek.game.GameLobby;
import hu.gamesgeek.game.Lobby;
import hu.gamesgeek.game.GameType;

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
}
