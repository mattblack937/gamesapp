package hu.gamesgeek.websocket.messagehandler;

import hu.gamesgeek.websocket.MessageType;

public abstract class AbstractMessageHandler<T extends Object> {

    abstract public void handleMessage(String userId, T wsMessage);

    public abstract MessageType getMessageType();

    public abstract Class getDtoClass();
}
