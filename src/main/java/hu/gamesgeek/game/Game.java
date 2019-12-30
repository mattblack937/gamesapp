package hu.gamesgeek.game;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.types.GameType;
import hu.gamesgeek.types.dto.GameDTO;
import hu.gamesgeek.types.dto.UserDTO;

import java.util.Collection;
import java.util.List;

public abstract class Game {

    protected UserDTO owner;
    protected List<UserDTO> users;

    public void initialize(Group group, Object settings){
        users = group.getUsers();
        owner = group.getOwner();
    }

    public GameDTO toGameDTO() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setGameType(getGameType());
        try {
            gameDTO.setData(new ObjectMapper().writeValueAsString(getDataDTO()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return gameDTO;
    }

    protected abstract Object getDataDTO();

    public abstract GameType getGameType();

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public abstract boolean legal(UserDTO user, Object move);

    public abstract void move(UserDTO user, Object move);

}
