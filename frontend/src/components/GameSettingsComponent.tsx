import React, {useState} from 'react';
import {Game, User} from "../util/types";
import {api} from "../util/API";
import {GameType} from "../util/enums";
import {AmobaComponent, AmobaDTO} from "./game/AmobaComponent";

type GameSettingsComponentProps = {
    user?: User
    game?: Game
}




export function GameSettingsComponent () {

    const [gameType, setGameType] = useState<GameType>();

    return (
        <div className={"game-settings"}>
            <div>
                GAME SETTINGS:
            </div>
            { renderGameSettings(gameType) }
        </div>
    );

    function renderGameSettings(gameType?: GameType) {
        if (!gameType){
            return (
                <div>
                    <div>
                        SELECT GAMETYPE:
                    </div>
                    <div onClick={()=> setGameType(GameType.AMOBA)}>
                        AMOBA
                    </div>
                </div>

            );
        }

        switch (gameType) {
            case GameType.AMOBA:
                return <AmobaSettings startGame={startGame}/>;

        }
    }

    async function startGame(settings: any) {
        await api.startGame(gameType!, settings);
    }

}

type AmobaSettingsProps = {
    startGame: (amobaSettingsDTO: AmobaSettingsDTO)=> void
}

type AmobaSettingsDTO = {
    meAs: MeAs
}


enum MeAs {
    RANDOM = "RANDOM",
    X = "X",
    O = "O"
}

function AmobaSettings( {startGame}: AmobaSettingsProps) {
    const [meAs, setMeAs] = useState<MeAs>(MeAs.RANDOM);

    return (
        <div>
            <div>
                AMOBA SETTINGS:
            </div>

            <div>
                <div>
                    Me as:
                </div>

                <label>
                    <input type="radio" value={MeAs.RANDOM} checked={meAs === MeAs.RANDOM} onChange={()=> setMeAs(MeAs.RANDOM)}/>
                    Random
                </label>
                <label>
                    <input type="radio" value={MeAs.X} checked={meAs === MeAs.X} onChange={()=> setMeAs(MeAs.X)}/>
                    X
                </label>
                <label>
                    <input type="radio" value={MeAs.O} checked={meAs === MeAs.O} onChange={()=> setMeAs(MeAs.O)}/>
                    O
                </label>
            </div>

            <div onClick={()=> startGame({meAs: meAs} as AmobaSettingsDTO)}>
                START GAME
            </div>
        </div>
    );
}
