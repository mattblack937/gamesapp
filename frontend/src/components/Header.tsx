import React, {useContext, useEffect} from 'react';
import {api} from "../util/API";

import {AppContext} from "../App";
import '../css/header.css';

import { ReactComponent as LogoutLogo } from '../svg/logout.svg';
import { ReactComponent as ReconnectLogo } from '../svg/reconnect.svg';

import {Key} from "ts-keycode-enum";

type MenuButtonProps = {
    text: string
    id: string
}

export function Header () {

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

    function Logout () {

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

        async function logout() {
            await api.logout();
            reconnect!();
        }

    }

}

















