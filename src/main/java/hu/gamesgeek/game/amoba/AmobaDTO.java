package hu.gamesgeek.game.amoba;

import hu.gamesgeek.types.dto.UserDTO;

public class AmobaDTO {
    private Boolean[] squares = new Boolean[9];
    private UserDTO nextPlayer;

    public Boolean[] getSquares() {
        return squares;
    }

    public void setSquares(Boolean[] squares) {
        this.squares = squares;
    }

    public UserDTO getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(UserDTO nextPlayer) {
        this.nextPlayer = nextPlayer;
    }
}
