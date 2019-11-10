import React from 'react';

export function Logout () {

    return (
        <form className={"logout"} action={"http://localhost:8080/logout"}>
            <input value={"KILÉPÉS"} type={"submit"}/>
        </form>
    );
}


