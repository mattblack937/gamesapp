package hu.gamesgeek.game.amoba;

import hu.gamesgeek.game.Game;
import hu.gamesgeek.game.GameAnnotation;
import hu.gamesgeek.game.GameState;
import hu.gamesgeek.game.Group;
import hu.gamesgeek.types.GameType;
import hu.gamesgeek.types.dto.GameDTO;
import hu.gamesgeek.types.dto.UserDTO;

@GameAnnotation(gameType = GameType.AMOBA, dtoClass = AmobaDTO.class, settingsClass = AmobaSettingsDTO.class)
public class AmobaGame extends Game {

//    X , O
//    0   1   2
//    3   4   5
//    6   7   8
    private Boolean[] squares = new Boolean[9];
    private UserDTO nextPlayer;
    private UserDTO playerX;

    @Override
    public void initialize(Group group, Object settings) {
        super.initialize(group, settings);
        AmobaSettingsDTO amobaSettingsDTO = (AmobaSettingsDTO) settings;
        playerX = users.get(MeAs.X.equals(amobaSettingsDTO.getMeAs()) ? 0 : MeAs.O.equals(amobaSettingsDTO.getMeAs()) ? 1 : Math.random() < 0.5 ? 0 : 1);
        nextPlayer = playerX;
    }


    @Override
    protected AmobaDTO getDataDTO() {
        AmobaDTO amobaDTO = new AmobaDTO();
        amobaDTO.setSquares(squares);
        amobaDTO.setNextPlayer(nextPlayer);
        amobaDTO.setPlayerX(playerX);
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

        squares[move.getSquare()] = playerX.equals(user) ? true : false;

        nextPlayer = users.get(nextPlayer.equals(users.get(0)) ? 1 : 0);
    }

    @Override
    public GameState getGameState() {
        boolean hasEmptySquare = false;
        for (int x = 0; x < 9; x++){
            if (squares[x] == null){
                hasEmptySquare = true;
                break;
            }
        }
        if (!hasEmptySquare){
            return GameState.ENDED;
        }

        int[][] rows = new int[][]{
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6},
        };

        return hasRow(rows) ? GameState.ENDED : GameState.IN_PROGRESS;
    }

    private boolean hasRow(int[][] rows) {
        for (int[] row: rows){
            if (squares[row[0]]!=null && squares[row[0]]==squares[row[1]] && squares[row[0]]==squares[row[2]]){
                return true;
            }
        }
        return false;
    }

    public enum MeAs {
        RANDOM, X, O
    }
}
