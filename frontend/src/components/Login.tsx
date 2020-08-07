import React, {FormEvent, useContext, useEffect, useState} from 'react';
import {User} from "../util/types";
import {api} from "../util/API";
import {func} from "prop-types";
import { Key } from 'ts-keycode-enum';

import {App, AppContext} from "../App";

export function Login () {

    const [userName, _setUserName] = useState("");
    const userNameRef = React.useRef(userName);
    const setUserName = (userName: string) => {
        userNameRef.current = userName;
        _setUserName(userName);
    };

    const [password, _setPassword] = useState("");
    const passwordRef = React.useRef(password);
    const setPassword = (password: string) => {
        passwordRef.current = password;
        _setPassword(password);
    };

    const { login, user } = useContext(AppContext);

    const listener = (event: KeyboardEvent) => {
        if (event.keyCode === Key.Enter && isFilled()) {
            console.log("login");
            login!(userNameRef.current, passwordRef.current);
        }
    };

    useEffect(() => {
        window.addEventListener('keyup', listener);
        return () => {
            window.removeEventListener('keyup', listener);
        };
    }, []);

    return(
        <div className={"login-wrapper"}>
                <div className={"login" + (isFilled() ? " active" : "")} onClick={()=> isFilled() && login!(userName, password)}>
                <input autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' name='username' onChange={(e)=>setUserName(e.target.value)}/>
                <input onClick={(e)=> e.stopPropagation()} type='password' name='password' onChange={(e)=> setPassword(e.target.value)}/>
                <div>Login</div>
            </div>
        </div>

    );

    function isFilled(){
        return userNameRef.current!="" && passwordRef.current!="";
    }

}


