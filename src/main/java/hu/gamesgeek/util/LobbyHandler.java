package hu.gamesgeek.util;

import hu.gamesgeek.game.GameLobby;
import hu.gamesgeek.game.Lobby;
import hu.gamesgeek.game.GameType;
import hu.gamesgeek.restful.user.UserService;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.dto.UserDTO;
import hu.gamesgeek.websocket.messagehandler.AbstractMessageHandler;
import hu.gamesgeek.websocket.messagehandler.MessageHandler;
import org.java_websocket.WebSocket;
import org.reflections.Reflections;

import java.util.*;

public class LobbyHandler {

    private static Map<Lobby, List<String>> lobbyUserIdList = new HashMap<>();

    private static List<Lobby> lobbyList = new ArrayList<>();

    public static Lobby createLobby(GameType gameType) {
        Reflections ref = new Reflections("hu.gamesgeek.game");
        for (Class<?> clazz : ref.getTypesAnnotatedWith(GameLobby.class)) {
            GameLobby gameLobby = clazz.getAnnotation(GameLobby.class);
            if (gameLobby.gameType().equals(gameType)){
                try {
                    Lobby lobby = (Lobby) clazz.getDeclaredConstructor().newInstance();
                    return lobby;
                } catch (Exception e) {

                }
            }
        }
        System.out.println("Could not create GameLobby for GameType: "+ gameType);
        return null;
    }

    public static void onStartGame(Lobby gameLobby) {
        lobbyList.remove(gameLobby);
    }

    public static void join(Lobby lobby, String userId) {
        List<String> userIds = lobbyUserIdList.containsKey(lobby) ? new ArrayList<>(lobbyUserIdList.get(lobby)) : new ArrayList<>();
        userIds.add(userId);
        lobbyUserIdList.put(lobby, userIds);
    }

    public static Lobby getLobbyByUserId(String userId) {
        for (Map.Entry<Lobby, List<String>> entry: lobbyUserIdList.entrySet()){
            if (entry.getValue().contains(userId)){
                return entry.getKey();
            }
        }
        return null;
    }

    public static List<String> getUserIdsByLobby(Lobby lobby) {
        return lobbyUserIdList.get(lobby);
    }

    public static List<UserDTO> getPlayersOfLobby(Lobby lobby) {
        List<UserDTO> players = new ArrayList<>();
        for (String userId: lobbyUserIdList.get(lobby)){
            players.add(ServiceHelper.getService(UserService.class).findUserDTOByUserId(userId));
        }
        return players;
    }
}
