import {GameType, MessageType} from "./enums";
import {Lobby} from "../components/game/Lobby";

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

export type LobbyType = {
    gameType: GameType,
    players: User[],
    id: string
}
