import React from 'react';
import {Game, User} from "../util/types";
import {api} from "../util/API";
import {GameType} from "../util/enums";
import {} from "../App";
import {AmobaComponent, AmobaDTO} from "./game/AmobaComponent";

type GameComponentProps = {
    user: User
    game: Game,
    setGameToNull: ()=> void
}

export function GameComponent ( { user, game, setGameToNull} : GameComponentProps) {
    // switch (game.gameType) {
    //     case GameType.AMOBA.valueOf():
    //         return  <AmobaComponent user={user} amobaDTO={(JSON.parse(game.data) as AmobaDTO)} gameState={game.gameState} setGameToNull={setGameToNull}/>;
    //
    //     default: return null;
    // }

}
