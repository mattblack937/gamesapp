package hu.gamesgeek.websocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateDTO {
    private List<UserDTO> users;
    private LobbyDTO lobby;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public LobbyDTO getLobby() {
        return lobby;
    }

    public void setLobby(LobbyDTO lobby) {
        this.lobby = lobby;
    }
}
