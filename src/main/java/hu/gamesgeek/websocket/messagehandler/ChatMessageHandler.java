package hu.gamesgeek.websocket.messagehandler;

import hu.gamesgeek.websocket.MessageType;
import hu.gamesgeek.websocket.dto.ChatMessageDTO;

@MessageHandler(messageType = MessageType.CHAT_MESSAGE, dtoClass = ChatMessageDTO.class)
public class ChatMessageHandler extends AbstractMessageHandler<ChatMessageDTO> {

    @Override
    public void handleMessage(String userId, ChatMessageDTO wsMessage) {
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
