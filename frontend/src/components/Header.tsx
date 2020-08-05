import React, {ReactNode, useContext, useState} from 'react';
import {UserList} from "./UserList";
import {Chat} from "./Chat";
import {ChatMessage, Group, User} from "../util/types";
import {api} from "../util/API";
import {UsersComponent} from "./UsersComponent";

import {Redirect, Route, Switch} from "react-router";
import {Login} from "./Login";
import {MainComponent} from "./MainComponent";
import {AppContext} from "../App";
import '../css/header.css';

import { ReactComponent as LogoutLogo } from '../svg/logout.svg';


type HeaderProps = {

}

type TabProps = {
    content: ReactNode
    id: string
}

type MenuButtonProps = {
    text: string
    id: string
}

export function Header ( {} : HeaderProps) {


    const { user } = useContext(AppContext);

    return (
        <div className={"header"}>
            <span>Welcome, {user!.name}</span>

            <HeaderButton id={"game"} text={"GAME"}/>
            <Logout/>
        </div>
    );

    function HeaderButton( {id, text}: MenuButtonProps) {
        return (
            <div >
                {text}
            </div>
        );
    }

    function UserName( {userName}: {userName:string}) {
        return (
            <span>
                {userName}
            </span>
        );
    }

    function Logout () {
        const { logout } = useContext(AppContext);

        return (
            <div className={"logout"} onClick={() => logout!()}>
                <LogoutLogo  />
            </div>
        );
    }



}

















