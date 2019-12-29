import {ChatMessage, UserToken, WSMessage} from "./types";
import {MessageType} from "./enums";
import App, {AppState} from "../App";

let webSocket = null as WebSocket | null;

export class WebSocketEntity {
    constructor(app: App, token: string){
        webSocket && webSocket.close();

        webSocket = new WebSocket("ws://localhost:9000") as WebSocket;

        webSocket.onmessage = (json) => {
            console.log(json);
            let message = JSON.parse(json.data) as WSMessage;
            console.log(message);
            switch (message.messageType) {
                case MessageType.STATE.valueOf():
                    app.updateState(JSON.parse(message.data) as AppState);
                    break;
                case MessageType.CHAT_MESSAGE.valueOf():
                    app.addChatMessage(JSON.parse(message.data) as ChatMessage);
                    break;
            }
        };

        webSocket.onopen = (response) => {
            app.state.user &&
            this.authenticateWebSocket(app.state.user.id, token);
        };

        webSocket.onerror = (response) => {
            app.onLogout();
        };

        webSocket.onclose = (response) => {
            app.onLogout();
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
    





