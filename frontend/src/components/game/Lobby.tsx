import React from 'react';
import {GameState, GameType} from "../../util/enums";
import {UserList} from "../UserList";
import {User} from "../../util/types";
import {api} from "../../util/API";
import {AmobaLobby} from "./Amoba/AmobaLobby";

export type LobbyProps = {
    gameState: GameState,
    gameType?: GameType
}

export type LobbyState = {

}

export function Lobby (props: LobbyProps) {
    const { gameState, gameType } = props;

    if (gameState === GameState.BROWSING){
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

    if (gameState === GameState.IN_LOBBY){
        switch (gameType) {
            case GameType.AMOBA:
                return ( <AmobaLobby {...props}/> );
        }
    }


    return (null);
}
