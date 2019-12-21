package hu.gamesgeek.websocket.messagehandler;

import hu.gamesgeek.websocket.MessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageHandler {
    MessageType messageType();

    Class<? extends Object> dtoClass();
}
