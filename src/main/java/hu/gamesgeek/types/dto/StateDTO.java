package hu.gamesgeek.types.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hu.gamesgeek.game.Group;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateDTO {
    private List<UserDTO> users;
    private GroupDTO group;

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    private GameDTO game;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public GroupDTO getGroup() {
        return group;
    }

    public void setGroup(GroupDTO group) {
        this.group = group;
    }



    public static class StateDTO_widthGroup{

        @JsonInclude(JsonInclude.Include.ALWAYS)
        private GroupDTO group;

        public GroupDTO getGroup() {
            return group;
        }

        public void setGroup(GroupDTO group) {
            this.group = group;
        }
    }
}
