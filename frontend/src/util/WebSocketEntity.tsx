import {Message, User} from "./types";
import {MessageType} from "./enums";
import App from "../App";

let webSocket = null as WebSocket | null;

export class WebSocketEntity {
    constructor(app: App, token: string){
        webSocket = new WebSocket("ws://localhost:9000") as WebSocket;

        webSocket.onmessage = (response) => {
            let message = JSON.parse(response.data) as Message;
            switch (message.type) {
                case MessageType.USER_LIST.valueOf():
                    app.setUsers(JSON.parse(message.data));
                    break;
                case MessageType.CHAT_MESSAGE.valueOf():
                    app.addChatMessage(JSON.parse(message.data));
                    break;
            }
        };

        webSocket.onopen = (response) => {
            app.state.user &&
            this.authenticateWebSocket(app.state.user, token);
        };

        webSocket.onerror = (response) => {
        };

        webSocket.onclose = (response) => {
        };
    }

    private authenticateWebSocket(user: User, token: string) {
        this.sendMessage(user, {token: token}, MessageType.AUTHENTICATE);
    }

    public sendMessage(user: User, data: any, messageType: MessageType){
        let messageDto = JSON.stringify({
            user: user,
            data: JSON.stringify(data),
            type: messageType
        });
        webSocket && webSocket.send(messageDto);
    }
}
    





