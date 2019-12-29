import React from 'react';
import {GameState, GameType} from "../../util/enums";
import {UserList} from "../UserList";
import {LobbyType, User} from "../../util/types";
import {api} from "../../util/API";
import {AmobaLobby} from "./Amoba/AmobaLobby";

export type LobbyProps = {
    lobby?: LobbyType,
    users: User[]
}


export function Lobby (lobbyProps: LobbyProps) {
    const { lobby } = lobbyProps;

    if (!lobby){
        return (
            <>
                SELECT GAME TYPE
                <div className={"menu-container"}>
                    <div className={"menu-element"} onClick={async ()=>await api.createLobby(GameType.AMOBA)}>
                        AMOBA
                    </div>
                </div>
            </>
        )
    }

    switch (lobby.gameType) {
        case GameType.AMOBA.valueOf():
            return (<AmobaLobby {...lobbyProps}/>);
    }

    return null;
}
