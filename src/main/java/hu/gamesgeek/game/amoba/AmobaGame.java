package hu.gamesgeek.game.amoba;

import hu.gamesgeek.game.Game;
import hu.gamesgeek.game.Lobby;


public class AmobaGame extends Game {

    public <LOBBY extends Lobby> AmobaGame(LOBBY gameLobby) {
        super(gameLobby);
    }

}
