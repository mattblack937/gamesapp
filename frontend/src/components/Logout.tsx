import React from 'react';
import {api} from "../util/API";

export function Logout () {

    return (
        <div className={"logout game-button"} onClick={() => api.logout()}>
            LOGOUT
        </div>
    );
}


