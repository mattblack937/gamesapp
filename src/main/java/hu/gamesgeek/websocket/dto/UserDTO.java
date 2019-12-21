package hu.gamesgeek.websocket.dto;

import hu.gamesgeek.restful.user.User;

import java.util.Objects;
import java.util.UUID;

public class UserDTO {
    String id;
    String name;

    public UserDTO() {

    }

    public UserDTO(User user) {
        this.id = String.valueOf(user.getId());
        this.name = user.getUserName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) &&
                Objects.equals(name, userDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
