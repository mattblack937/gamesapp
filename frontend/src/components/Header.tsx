import React, {useContext, useEffect} from 'react';
import {api} from "../util/API";

import {AppContext, ContentMode} from "../App";
import '../css/header.css';

import {ReactComponent as LogoutLogo} from '../svg/logout.svg';
import {ReactComponent as ReconnectLogo} from '../svg/reconnect.svg';

import {Key} from "ts-keycode-enum";

type MenuButtonProps = {
    text: string
    contentMode: ContentMode
}

export function Header () {

    const { user, reconnect, setContentMode } = useContext(AppContext);

    return (
        <div className={"header"}>

            <div className={"header-buttons"}>
                <HeaderButton text={"HOME"} contentMode={ContentMode.HOME} />
                {/*<HeaderButton text={"HOME"} contentMode={ContentMode.GAME} />*/}
                <HeaderButton text={"CHAT"} contentMode={ContentMode.CHAT}/>
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

    function HeaderButton( {contentMode, text}: MenuButtonProps) {
        return (
            <div className={"header-button"} onClick={()=> setContentMode!(contentMode)}>
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

















