import React from 'react';
import {Game, User} from "../util/types";
import {api} from "../util/API";
import {GameType} from "../util/enums";
import {AppState} from "../App";
import {AmobaComponent, AmobaDTO} from "./game/AmobaComponent";

type GameComponentProps = {
    user: User
    game: Game
}

export function GameComponent ( { user, game} : GameComponentProps) {
    switch (game.gameType) {
        case GameType.AMOBA.valueOf():
            return  <AmobaComponent user={user} amobaDTO={(JSON.parse(game.data) as AmobaDTO)} />;

        default: return null;
    }

}
