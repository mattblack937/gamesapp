package hu.gamesgeek.websocket.dto;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StateDTO {
    List<UserDTO> users;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
