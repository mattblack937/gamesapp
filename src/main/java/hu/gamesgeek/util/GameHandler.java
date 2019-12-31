package hu.gamesgeek.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gamesgeek.WSServer;
import hu.gamesgeek.game.Game;
import hu.gamesgeek.game.GameAnnotation;
import hu.gamesgeek.game.GameState;
import hu.gamesgeek.game.Group;
import hu.gamesgeek.game.amoba.AmobaDTO;
import hu.gamesgeek.game.amoba.AmobaSettingsDTO;
import hu.gamesgeek.model.user.UserService;
import hu.gamesgeek.types.GameType;
import hu.gamesgeek.types.MessageType;
import hu.gamesgeek.types.dto.GameDTO;
import hu.gamesgeek.types.dto.StateDTO;
import hu.gamesgeek.types.dto.UserDTO;
import hu.gamesgeek.websocket.WSMessage;
import hu.gamesgeek.websocket.messagehandler.AbstractMessageHandler;
import hu.gamesgeek.websocket.messagehandler.MessageHandler;
import hu.gamesgeek.websocket.messagehandler.StateMessageHandler;
import org.java_websocket.WebSocket;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private static ObjectMapper mapper = new ObjectMapper();

    private static List<Game> games = new ArrayList<>();

    public static boolean canStartGame(Group group, GameType gameType, Object settings) {

        switch (gameType){
            case AMOBA:
                return group.getUsers().size() == 2 && settings instanceof AmobaSettingsDTO;
        }

        return false;
    }

    public static void startGame(Group group, GameType gameType, Object settings) {
        Game game = newGameByGameType(gameType);
        game.initialize(group, settings);

        games.add(game);

        StateDTO stateDTO = new StateDTO();
        stateDTO.setGame(game.toGameDTO());

        WSMessage wsMessage  = new WSMessage();
        wsMessage.setMessageType(MessageType.STATE);
        try {
            wsMessage.setData(mapper.writeValueAsString(stateDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for (UserDTO user: group.getUsers()){
            for (WebSocket webSocket: ConnectionHandler.getWebSocketsByUserId(user.getId())){
                WSServer.sendMessage(webSocket, wsMessage);
            }
        }
    }

    private static Game newGameByGameType(GameType gameType) {
        Reflections ref = new Reflections("hu.gamesgeek.game");
        for (Class<?> clazz : ref.getTypesAnnotatedWith(GameAnnotation.class)) {
            GameAnnotation gameAnnotation = clazz.getAnnotation(GameAnnotation.class);
            if (gameAnnotation.gameType().equals(gameType)){
                try {
                    Game game = (Game) clazz.getDeclaredConstructor().newInstance();
                    return game;
                } catch (Exception e) {

                }
            }
        }
        System.out.println("Could not create GameLobby for GameType: "+ gameType);
        return null;
    }

    public static Game getGameOfUser(UserDTO user) {
        for (Game game: games){
            if (game.getUsers().contains(user)){
                return game;
            }
        }
        return null;
    }

    public static void updateUsers(Game game) {
        WSMessage wsMessage = new WSMessage();
        wsMessage.setMessageType(MessageType.STATE);
        StateDTO stateDTO = new StateDTO();
        GameDTO gameDTO = game.toGameDTO();
        stateDTO.setGame(gameDTO);
        try {
            wsMessage.setData(mapper.writeValueAsString(stateDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for (UserDTO user: game.getUsers()){
            for(WebSocket webSocket: ConnectionHandler.getWebSocketsByUserId(user.getId())){
                WSServer.sendMessage(webSocket, wsMessage);
            }
        }

        if (GameState.ENDED.equals(gameDTO.getGameState())){
            removeGame(game);
        }
    }

    private static void removeGame(Game game) {
        games.remove(game);
    }

    public static Object readSettingsFromJSON(GameType gameType, String gameSettingsJSON) {
        Class clazz = getSettingsClazzOfGameType(gameType);

        if (clazz == void.class){
            return null;
        }

        try {
            return mapper.readValue(gameSettingsJSON, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Class getSettingsClazzOfGameType(GameType gameType) {
        Reflections ref = new Reflections("hu.gamesgeek.game");
        for (Class<?> clazz : ref.getTypesAnnotatedWith(GameAnnotation.class)) {
            GameAnnotation gameAnnotation = clazz.getAnnotation(GameAnnotation.class);
            if (gameAnnotation.gameType().equals(gameType)){
                try {
                    return gameAnnotation.settingsClass();
                } catch (Exception e) {

                }
            }
        }
        System.out.println("Could not get SettingsClazzOfGameType: "+ gameType);
        return null;
    }
}
