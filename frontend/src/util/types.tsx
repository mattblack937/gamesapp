import {MessageType} from "./enums";

export type User = {
    id: string,
    name: string
}

export type WSMessage = {
    data: any,
    messageType: MessageType
}

export type ChatMessage = {
    message: string,
    user: User
}

export type UserToken = {
    userId: string,
    token: string
}
