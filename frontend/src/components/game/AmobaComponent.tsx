import React from 'react';
import {UserList} from "../UserList";
import {Chat} from "../Chat";
import {ChatMessage, Group, User} from "../../util/types";
import {api} from "../../util/API";
import {UsersComponent} from "../UsersComponent";


type AmobaComponentProps = {
    amobaDTO: AmobaDTO,
    user: User
}


export type AmobaDTO = {
    squares: boolean[],
    nextPlayer: User
}

export type AmobaMoveDTO = {
    square: number
}


export function AmobaComponent ( {amobaDTO, user} : AmobaComponentProps) {
    const { squares, nextPlayer } = amobaDTO;
    const myTurn = nextPlayer.id == user.id;

    return(
        <div className={"amoba"}>
            <div>
                <div>
                    {renderSquare(0)}
                </div>
                <div>
                    {renderSquare(1)}
                </div>
                <div>
                    {renderSquare(2)}
                </div>
            </div>
            <div>
                <div>
                    {renderSquare(3)}
                </div>
                <div>
                    {renderSquare(4)}
                </div>
                <div>
                    {renderSquare(5)}
                </div>
            </div>
            <div>
                <div>
                    {renderSquare(6)}
                </div>
                <div>
                    {renderSquare(7)}
                </div>
                <div>
                    {renderSquare(8)}
                </div>
            </div>
        </div>
    );

    function renderSquare(n: number) {
        const value = squares[n];
        return(
            <div onClick={async ()=> click(n)}>
                {value===null ? "" : value==true ? "X" : "O"}
            </div>
        );
    }

    async function click(n: number) {
        const moveDTO = {
            square: n
        } as AmobaMoveDTO;

        myTurn && await api.move(moveDTO);
    }
}
