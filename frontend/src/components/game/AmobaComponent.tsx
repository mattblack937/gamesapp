import React from 'react';
import {User} from "../../util/types";
import {api} from "../../util/API";
import {GameState} from "../../util/enums";
import {SVG} from "../SVG";


export function amoba_x(size: number) {
    return(
        <svg height={size} width={size} className={"amoba_x"}>
            <line x1={size/10}  y1={size/10}  x2={size/10*9}  y2={size/10*9} strokeWidth={size/5} stroke-linecap="round" />
            <line x1={size/10} y1={size/10*9} x2={size/10*9} y2={size/10} strokeWidth={size/5} stroke-linecap="round" />
        </svg>
    );
}

export function amoba_o(size: number) {
    return(
        <svg className="amoba_o">
            <circle cx={size/2} cy={size/2} stroke-width={size/5} r={size/100*40} />
        </svg>
    );
}





type AmobaComponentProps = {
    amobaDTO: AmobaDTO,
    user: User,
    gameState: GameState,
    setGameToNull: ()=> void
}


export type AmobaDTO = {
    squares: boolean[],
    nextPlayer: User,
    playerX: User
}

export type AmobaMoveDTO = {
    square: number
}


export function AmobaComponent ( {amobaDTO, user, gameState, setGameToNull} : AmobaComponentProps) {
    const { squares, nextPlayer, playerX } = amobaDTO;
    const myTurn = nextPlayer.id == user.id;

    return(
        <div className={"amoba"}>

            <div className={"amoba-board"}>
                <div className={"row"}>
                    {renderSquare(0)}
                    {renderSquare(1)}
                    {renderSquare(2)}
                </div>
                <div className={"row"}>
                    {renderSquare(3)}
                    {renderSquare(4)}
                    {renderSquare(5)}
                </div>
                <div className={"row"}>
                    {renderSquare(6)}
                    {renderSquare(7)}
                    {renderSquare(8)}
                </div>
            </div>

            <div className={"amoba-header"}>
                <div className="amoba-header-row">
                    You are: {playerX.id===user.id ? amoba_x(30) : amoba_o(30)}
                </div>
                {
                    nextPlayer &&
                    <div className="amoba-header-row">
                        Next player: {nextPlayer.id===playerX.id ? amoba_x(30) : amoba_o(30)}
                    </div>
                }
            </div>

            {
                gameState===GameState.ENDED &&
                <>
                    <div>
                        GAME ended
                    </div>
                    <div onClick={()=> setGameToNull()}>
                        OK
                    </div>
                </>
            }
        </div>
    );

    function renderSquare(n: number) {
        const value = squares[n];
        return(
            <div className={"square" + (value===null ? " empty" : "")} onClick={async ()=> click(n)}>
                {value===null ? "" : value==true ? amoba_x(100) : amoba_o(100)}
            </div>
        );
    }

    async function click(n: number) {
        const moveDTO = {
            square: n
        } as AmobaMoveDTO;

        console.log("ggg", gameState===GameState.IN_PROGRESS, gameState);
        myTurn && !squares[n] && gameState===GameState.IN_PROGRESS.valueOf() && await api.move(moveDTO);
    }
}
