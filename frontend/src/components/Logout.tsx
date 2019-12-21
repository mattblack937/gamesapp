import React from 'react';
import {api} from "../util/API";

export function Logout () {

    return (
        <input value={"KILÉPÉS"} onClick={() => api.logout()}/>
    );
}


