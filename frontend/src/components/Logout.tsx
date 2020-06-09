import React, {useContext} from 'react';
import {api} from "../util/API";
import {AppContext} from "../App";

export function Logout () {

    const { logout } = useContext(AppContext);

    return (
        <div className={"logout game-button"} onClick={() => logout!()}>
            LOGOUT
        </div>
    );
}


