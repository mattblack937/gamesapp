import React from 'react';
import {api} from "../util/API";

export function Logout () {

    return (
        <div className={"logout"} onClick={() => api.logout()}>
            LOGOUT
        </div>
    );
}


