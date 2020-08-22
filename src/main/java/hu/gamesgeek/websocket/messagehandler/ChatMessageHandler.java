package hu.gamesgeek.websocket.messagehandler;

import hu.gamesgeek.types.MessageType;
import hu.gamesgeek.types.dto.ChatMessageDTO;
import hu.gamesgeek.types.dto.UserDTO;
import hu.gamesgeek.util.ConnectionHandler;
import org.java_websocket.WebSocket;

@MessageHandler(messageType = MessageType.CHAT_MESSAGE, dtoClass = ChatMessageDTO.class)
public class ChatMessageHandler extends AbstractMessageHandler<ChatMessageDTO> {



    @Override
    public void handleMessage(Long userId, ChatMessageDTO wsMessage) {
        String a = "a";
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT_MESSAGE;
    }

    @Override
    public Class getDtoClass() {
        return ChatMessageDTO.class;
    }

}
