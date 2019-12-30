package hu.gamesgeek.game.amoba;

import hu.gamesgeek.game.Game;
import hu.gamesgeek.game.GameAnnotation;
import hu.gamesgeek.game.Group;
import hu.gamesgeek.types.GameType;
import hu.gamesgeek.types.dto.UserDTO;

@GameAnnotation(gameType = GameType.AMOBA, dtoClass = AmobaDTO.class, settingsClass = AmobaSettingsDTO.class)
public class AmobaGame extends Game {

//    X , O
//    0   1   2
//    3   4   5
//    6   7   8
    private Boolean[] squares = new Boolean[9];
    private UserDTO nextPlayer;
    private boolean nextPlayerIsX = true;

    @Override
    public void initialize(Group group, Object settings) {
        super.initialize(group, settings);
        AmobaSettingsDTO amobaSettingsDTO = (AmobaSettingsDTO) settings;
        nextPlayer = users.get(MeAs.X.equals(amobaSettingsDTO.getMeAs()) ? 0 : MeAs.O.equals(amobaSettingsDTO.getMeAs()) ? 1 : Math.random() < 0.5 ? 0 : 1);
    }


    @Override
    protected AmobaDTO getDataDTO() {
        AmobaDTO amobaDTO = new AmobaDTO();
        amobaDTO.setSquares(squares);
        amobaDTO.setNextPlayer(nextPlayer);
        return amobaDTO;
    }

    @Override
    public GameType getGameType() {
        return GameType.AMOBA;
    }

    @Override
    public boolean legal(UserDTO user, Object o) {
        if (!(o instanceof AmobaMoveDTO)){
            return false;
        }

        AmobaMoveDTO move = (AmobaMoveDTO) o;
        return nextPlayer.equals(user) && move.getSquare()<=8 && move.getSquare()>=0 && squares[move.getSquare()]==null;
    }

    @Override
    public void move(UserDTO user, Object o) {
        AmobaMoveDTO move = (AmobaMoveDTO) o;

        squares[move.getSquare()] = nextPlayerIsX ? true : false;

        nextPlayerIsX = !nextPlayerIsX;
        nextPlayer = nextPlayer.equals(users.get(0)) ? users.get(1) : users.get(0);
    }


    public enum MeAs {
        RANDOM, X, O
    }
}
