import React, {FormEvent, useContext, useEffect, useState} from 'react';
import {User} from "../util/types";
import {api} from "../util/API";
import {func} from "prop-types";
import { Key } from 'ts-keycode-enum';

import {App, AppContext} from "../App";
import { ReactComponent as PlusSign } from '../svg/plus.svg';
import { ReactComponent as BackSign } from '../svg/back.svg';

enum Mode {
    LOGIN, REGISTRATION
}

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

    const [userNameNew, _setUserNameNew] = useState("");
    const userNameNewRef = React.useRef(userNameNew);
    const setUserNameNew = (userNameNew: string) => {
        userNameNewRef.current = userNameNew;
        _setUserNameNew(userNameNew);
    };

    const [passwordNew, _setPasswordNew] = useState("");
    const passwordNewRef = React.useRef(passwordNew);
    const setPasswordNew = (passwordNew: string) => {
        passwordNewRef.current = passwordNew;
        _setPasswordNew(passwordNew);
    };

    const [passwordAgain, _setPasswordAgain] = useState("");
    const passwordAgainRef = React.useRef(passwordAgain);
    const setPasswordAgain = (passwordAgain: string) => {
        passwordAgainRef.current = passwordAgain;
        _setPasswordAgain(passwordAgain);
    };

    const [mode, _setMode] = useState<Mode>(Mode.LOGIN);
    const modeRef = React.useRef(mode);
    const setMode = (mode: Mode) => {
        modeRef.current = mode;
        _setMode(mode);
    };



    const { login, createNewAccount } = useContext(AppContext);

    const listener = (event: KeyboardEvent) => {
        if (event.keyCode === Key.Enter && isFilled()) {
            submit();
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
            <div className={"login-z"}>
                <div className={"login" + (isFilled() ? " active" : "")} onClick={()=> isFilled() && submit()}>
                    {mode === Mode.LOGIN ?
                        <div id={"1"}>
                            <div className={"input-with-default-value user-name"}>
                                <input value={userName} required={true} autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' onChange={(e)=>setUserName(e.target.value)}/>
                                <div></div>
                            </div>
                            <div className={"input-with-default-value password"}>
                                <input value={password} required={true} onClick={(e)=> e.stopPropagation()} type='password' onChange={(e)=> setPassword(e.target.value)}/>
                                <div></div>
                            </div>
                            <div>Login</div>
                        </div> :
                        <div id={"2"}>
                            <div className={"input-with-default-value user-name"}>
                                <input value={userNameNew} required={true} autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' onChange={(e)=>setUserNameNew(e.target.value)}/>
                                <div></div>
                            </div>
                            <div className={"input-with-default-value password"}>
                                <input value={passwordNew} required={true} onClick={(e)=> e.stopPropagation()} type='password' onChange={(e)=> setPasswordNew(e.target.value)}/>
                                <div></div>
                            </div>
                            <div className={"input-with-default-value password-again"}>
                                <input value={passwordAgain} required={true} onClick={(e)=> e.stopPropagation()} type='password' onChange={(e)=> setPasswordAgain(e.target.value)}/>
                                <div></div>
                            </div>
                            <div>Create Account</div>
                        </div>
                    }
                </div>

                {isFilled() ||
                <div className={"sign"} onClick={()=> changeMode()}>
                    {mode === Mode.LOGIN ?
                        <PlusSign/> :
                        <BackSign/>
                    }
                </div>
                }
            </div>
        </div>

    );

    function changeMode() {
        if (mode === Mode.LOGIN){
            setMode(Mode.REGISTRATION);
            setUserName("");
            setPassword("");
        } else {
            setMode(Mode.LOGIN);
            setUserNameNew("");
            setPasswordNew("");
            setPasswordAgain("");
        }
    }

    function submit() {
        if (mode === Mode.LOGIN){
            login!(userNameRef.current, passwordRef.current)
        } else {
            if (passwordNewRef.current === passwordAgainRef.current){
                createNewAccount!(userNameNewRef.current, passwordNewRef.current);
            } else {
                // TODO error
            }
        }
    }

    function isFilled(){
        if (modeRef.current === Mode.LOGIN){
            return userNameRef.current!="" && passwordRef.current!="";
        } else {
            return userNameNewRef.current!="" && passwordNewRef.current!="" && passwordAgainRef.current!="";
        }
    }

}


