package hu.gamesgeek.game;

import hu.gamesgeek.types.dto.GroupDTO;
import hu.gamesgeek.types.dto.UserDTO;
import hu.gamesgeek.util.GroupHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Group {

    public Group(UserDTO owner){
        this.owner = owner;
        users.add(owner);
    }

    private UserDTO owner;
    private List<UserDTO> users = new ArrayList<>();


    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public void addUser(UserDTO user){
        users.add(user);
    }

    public void joinUser(UserDTO user) {
        users.add(user);
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void joinGroup(Group groupOfUser) {
        users.addAll(groupOfUser.users);
        GroupHandler.destroyGroup(groupOfUser);
    }

    public GroupDTO toGroupDTO() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setOwner(owner);
        groupDTO.setUsers(users);
        return groupDTO;
    }
}
