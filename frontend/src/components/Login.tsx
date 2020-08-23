import React, {useContext, useEffect, useState} from 'react';
import {api} from "../util/API";
import {Key} from 'ts-keycode-enum';

import {AppContext} from "../App";
import {ReactComponent as PlusSign} from '../svg/plus.svg';
import {ReactComponent as BackSign} from '../svg/back.svg';

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

    const [error, setError] = useState<string>("");

    const { reconnect } = useContext(AppContext);

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
                        <>
                            <div className={"input-with-default-value user-name"}>
                                <input key={1} value={userName} required={true} autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' onChange={(e)=>setUserName(e.target.value)}/>
                                <div></div>
                            </div>
                            <div className={"input-with-default-value password"}>
                                <input key={2} value={password} required={true} onClick={(e)=> e.stopPropagation()} type='password' onChange={(e)=> setPassword(e.target.value)}/>
                                <div></div>
                            </div>
                            <div>Login</div>
                        </> :
                        <>
                            <div className={"input-with-default-value user-name-new"}>
                                <input key={3} autoComplete={"new-password"} value={userNameNew} required={true} autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' onChange={(e)=>setUserNameNew(e.target.value)}/>
                                <div></div>
                            </div>
                            <div className={"input-with-default-value password-new"}>
                                <input key={4} value={passwordNew} required={true} onClick={(e)=> e.stopPropagation()} type='password' onChange={(e)=> setPasswordNew(e.target.value)}/>
                                <div></div>
                            </div>
                            <div className={"input-with-default-value password-again"}>
                                <input key={5} value={passwordAgain} required={true} onClick={(e)=> e.stopPropagation()} type='password' onChange={(e)=> setPasswordAgain(e.target.value)}/>
                                <div></div>
                            </div>
                            <div>Create Account</div>
                        </>
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

                {error &&
                <div className={"error"} onClick={()=> setError("")}>
                    {error}
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
        setError("");
    }

    async function submit() {
        if (modeRef.current === Mode.LOGIN){
            let error = await api.login(userNameRef.current, passwordRef.current);
            if(!error){
                reconnect!();
            } else {
                setError(error.message);
            }
        } else {
            if (passwordNewRef.current === passwordAgainRef.current){
                const error = await api.createNewAccount!(userNameNewRef.current, passwordNewRef.current);
                if(error){
                    setError(error.message);
                } else {
                    reconnect!();
                }
            } else {
                setError("Passwords must match");
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


