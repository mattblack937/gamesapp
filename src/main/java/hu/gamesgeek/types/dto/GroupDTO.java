package hu.gamesgeek.types.dto;

import hu.gamesgeek.types.GameType;

import java.util.ArrayList;
import java.util.List;

public class GroupDTO {

    private UserDTO owner;
    private List<UserDTO> users;

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
}
