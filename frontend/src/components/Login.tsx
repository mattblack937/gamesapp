import React, {FormEvent, useContext, useState} from 'react';
import {User} from "../util/types";
import {api} from "../util/API";
import {func} from "prop-types";

import {App, AppContext} from "../App";

export function Login () {

    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const { login } = useContext(AppContext);

    return(
        <div className={"login" + (filled() ? " active" : "")} onClick={()=> filled() && login!(userName, password)}>
            <input autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' name='username' onChange={(e)=>setUserName(e.target.value)}/>
            <input onClick={(e)=> e.stopPropagation()} type='password' name='password' onChange={(e)=> setPassword(e.target.value)}/>
            <div>Login</div>
        </div>
    );

    function filled(){
        return userName!="" && password!="";
    }

}


