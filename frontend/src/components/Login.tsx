import React, {FormEvent, useState} from 'react';
import {User} from "../util/types";
import {api} from "../util/API";

type LoginProps = {
    refresh:  () => void
}

export function Login (props: LoginProps) {

    const [userName, setUserName] = useState();
    const [password, setPassword] = useState();
    const [errorMessage, setErrorMessage] = useState();

    return (
        <div className={"login"}>

            <input onChange={(e)=>setUserName(e.target.value)} type='text' name='username'/>
            <input onChange={(e)=>setPassword(e.target.value)} type='password' name='password' />
            <div onClick={()=>login()} > Bejelentkezés </div>

            {errorMessage &&
                <div>
                    {errorMessage}
                </div>
            }
        </div>
    );

    async function login() {

        const res = await api.login(userName, password);

        if (res){
            await props.refresh();
        } else {
            setErrorMessage("ERROR");
        }

        // if (res){
        //     let user = await api.getUser();
        //     props.setUser(user);
        //
        //     props.registerSocket();
        // } else {
        //     setErrorMessage("ERROR");
        // }
    }
}


