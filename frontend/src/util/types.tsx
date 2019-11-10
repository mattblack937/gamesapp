import {MessageType} from "./enums";

export type User = {
    id: string,
    name: string
}

export type Message = {
    user: User,
    type: MessageType,
    data: any
}

export type ChatMessage = {
    message: string,
    user: User,
    date?: Date
}