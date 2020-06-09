import {ChatMessage, User, UserToken, WSMessage} from "./types";
import {MessageType} from "./enums";
import App, {AppContext} from "../App";
import {useContext} from "react";
import {log} from "util";

let webSocket = null as WebSocket | null;

export class WebSocketEntity {

    constructor(token: string){
        const { logout, user } = useContext(AppContext);

        webSocket && webSocket.close();
        webSocket = new WebSocket("ws://localhost:9000") as WebSocket;

        webSocket.onopen = (response) => {
            this.authenticateWebSocket(user!.id, token);
        };

        webSocket.onerror = (response) => {
            logout!();
        };

        webSocket.onclose = (response) => {
            logout!();
        };

        webSocket.onmessage = (json) => {
            console.log(json);
            let message = JSON.parse(json.data) as WSMessage;
            console.log(message);
            switch (message.messageType) {
                case MessageType.STATE.valueOf():
                    // app.updateState(JSON.parse(message.data) as AppState);
                    break;
                case MessageType.CHAT_MESSAGE.valueOf():
                    // app.addChatMessage(JSON.parse(message.data) as ChatMessage);
                    break;
                case MessageType.INVITE.valueOf():
                    // app.addInvite(JSON.parse(message.data) as User);
                    break;
            }
        };
    }

    private authenticateWebSocket(userId: string, token: string) {
        this.sendMessage({
            userId,
            token
        } as UserToken, MessageType.USER_TOKEN);
    }

    public sendMessage(data: any, messageType: MessageType){
        let messageDto = JSON.stringify({
                data: JSON.stringify(data),
                messageType: messageType
            } as WSMessage
        );
        webSocket && webSocket.send(messageDto);
    }
}
    





