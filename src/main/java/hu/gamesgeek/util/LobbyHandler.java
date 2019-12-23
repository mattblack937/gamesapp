package hu.gamesgeek.util;

import hu.gamesgeek.game.GameLobby;
import hu.gamesgeek.game.Lobby;
import hu.gamesgeek.game.GameType;
import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.messagehandler.AbstractMessageHandler;
import hu.gamesgeek.websocket.messagehandler.MessageHandler;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class LobbyHandler {

    private static List<Lobby> lobbyList = new ArrayList<>();

    public static Lobby createLobby(GameType gameType) {
        Reflections ref = new Reflections("hu.gamesgeek.game");
        for (Class<?> clazz : ref.getTypesAnnotatedWith(GameLobby.class)) {
            GameLobby gameLobby = clazz.getAnnotation(GameLobby.class);
            if (gameLobby.gameType().equals(gameType)){
                try {
                    Lobby lobby = (Lobby) clazz.getDeclaredConstructor().newInstance();
                    lobbyList.add(lobby);
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

}
