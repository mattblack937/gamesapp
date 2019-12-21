package hu.gamesgeek.websocket.messagehandler;

public abstract class AbstractMessageHandler<T extends Object> {

    abstract public void handleMessage(String userId, T wsMessage);
}
