import React from 'react';
import {User} from "../../util/types";
import {api} from "../../util/API";
import {GameState} from "../../util/enums";
import {SVG} from "../SVG";


export function amoba_x(size: number) {
    return(
        <svg className={"amoba_x"} version="1" id="X" height={size+"px"} width={size+"px"} viewBox="0 0 348.333 348.334">
            <g>
                <path d="M336.559,68.611L231.016,174.165l105.543,105.549c15.699,15.705,15.699,41.145,0,56.85
                    c-7.844,7.844-18.128,11.769-28.407,11.769c-10.296,0-20.581-3.919-28.419-11.769L174.167,231.003L68.609,336.563
                    c-7.843,7.844-18.128,11.769-28.416,11.769c-10.285,0-20.563-3.919-28.413-11.769c-15.699-15.698-15.699-41.139,0-56.85
                    l105.54-105.549L11.774,68.611c-15.699-15.699-15.699-41.145,0-56.844c15.696-15.687,41.127-15.687,56.829,0l105.563,105.554
                    L279.721,11.767c15.705-15.687,41.139-15.687,56.832,0C352.258,27.466,352.258,52.912,336.559,68.611z"/>
            </g>
        </svg>
    );
}

export function amoba_y(size: number) {
    return(
        <svg className="amoba_y">
            <circle cx={size/2} cy={size/2} stroke-width={size/5} r={size/100*40}>
            </circle>
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
                    You are: {playerX.id===user.id ? amoba_x(30) : amoba_y(30)}
                </div>
                {
                    nextPlayer &&
                    <div className="amoba-header-row">
                        Next player: {nextPlayer.id===playerX.id ? amoba_x(30) : amoba_y(30)}
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
                {value===null ? "" : value==true ? amoba_x(100) : amoba_y(100)}
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
