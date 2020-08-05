package hu.gamesgeek.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.GameWebSocketServer;
import hu.gamesgeek.game.Group;
import hu.gamesgeek.types.MessageType;
import hu.gamesgeek.types.dto.StateDTO;
import hu.gamesgeek.types.dto.UserDTO;
import hu.gamesgeek.websocket.WSMessage;
import org.java_websocket.WebSocket;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupHandler {
    private static ObjectMapper mapper = new ObjectMapper();

    private static List<Group> groups = new ArrayList<>();

    public static Group createGroup(UserDTO owner){
        Group group = new Group(owner);
        groups.add(group);
        return group;
    }

    public static Group getGroupOfUser(UserDTO user) {
        for (Group group: groups){
            if (group.getUsers().contains(user)){
                return group;
            }
        }
        return null;
    }

    public static void destroyGroup(Group groupOfUser) {
        groups.remove(groupOfUser);
    }

    public static void updateUser(UserDTO user) {
        StateDTO.StateDTO_widthGroup stateDTO = new StateDTO.StateDTO_widthGroup();

        Group group = getGroupOfUser(user);
        stateDTO.setGroup(group==null ? null : group.toGroupDTO());

        WSMessage wsMessage  = new WSMessage();
        wsMessage.setMessageType(MessageType.STATE);
        try {
            wsMessage.setData(mapper.writeValueAsString(stateDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for (WebSocket webSocket: ConnectionHandler.getWebSocketsByUserId(user.getId())){
            GameWebSocketServer.sendMessage(webSocket, wsMessage);
        }
    }

    public static void updateGroup(Group group) {
        group.getUsers().forEach(user-> updateUser(user));
    }

    public static void sendInvite(UserDTO fromUser, String toUserId) {
        WSMessage wsMessage  = new WSMessage();
        wsMessage.setMessageType(MessageType.INVITE);

        try {
            wsMessage.setData(mapper.writeValueAsString(fromUser));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        GameWebSocketServer.sendMessage(toUserId, wsMessage);
    }

    public static void leaveGroup(UserDTO user) {
        Group group = getGroupOfUser(user);
        List<UserDTO> users = new ArrayList<>(group.getUsers());

        //Egyszemélyes group-okat megszüntetjük
        if (group.getUsers().size() == 2){
            destroyGroup(group);
            users.forEach(u-> updateUser(u));
            return;
        }

        //Kivesszük a users-ből
        group.setUsers(group.getUsers().stream().filter(u-> !u.equals(user)).collect(Collectors.toList()));

        //Ha ő volt az owner, a group-ja owner-t vált
        if (group.getOwner().equals(user)){
            group.setOwner(group.getUsers().get(0));
        }

        users.forEach(u-> updateUser(u));
    }
}
