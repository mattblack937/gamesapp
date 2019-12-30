import {GameType, MessageType} from "./enums";

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

export type Group = {
    owner: User,
    users: User[]
}
export type Invite = {
    fromUser: User
}

export type Game = {
    gameType: GameType,
    data: any
}


export type UserToken = {
    userId: string,
    token: string
}

export type LobbyType = {
    gameType: GameType,
    players: User[],
    id: string
}
