import React, {ReactNode, useContext, useEffect, useState} from 'react';
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
import { ReactComponent as ReconnectLogo } from '../svg/reconnect.svg';


import {Key} from "ts-keycode-enum";


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


    const { user, reconnect } = useContext(AppContext);



    return (
        <div className={"header"}>

            <div className={"header-buttons"}>
                <HeaderButton id={"game"} text={"GAME"}/>
                <HeaderButton id={"s"} text={"CHAT"}/>
                <HeaderButton id={"s"} text={"PROFILE"}/>
                <HeaderButton id={"s"} text={"PROFILE"}/>


            </div>

            <div className={"header-left"}>
                <div className={"welcome"}>Welcome, {user!.name}</div>
                <div className={"reconnect"} onClick={()=> reconnect!()}>
                    <ReconnectLogo />
                </div>
            </div>


            <Logout/>
        </div>
    );

    function HeaderButton( {id, text}: MenuButtonProps) {
        return (
            <div className={"header-button"}>
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

        const listener = (event: KeyboardEvent) => {
            if (event.keyCode === Key.Escape) {
                logout!();
            }
        };

        useEffect(() => {
            window.addEventListener('keyup', listener);
            return () => {
                window.removeEventListener('keyup', listener);
            };
        }, []);

        return (
            <div className={"logout"} onClick={() => logout!()}>
                <LogoutLogo />
            </div>
        );
    }



}

















