import React, {FormEvent, useState} from 'react';
import {User} from "../util/types";
import {api} from "../util/API";

type LoginProps = {
    setUser: (user: User) => void,
    registerSocket: () => void
}

export function Login (props: LoginProps) {

    const [userName, setUserName] = useState();
    const [password, setPassword] = useState();
    const [errorMessage, setErrorMessage] = useState();

    return (
        <div className={"login"}>
            <form onSubmit={(event: FormEvent)=>{login(event)}}>
                <input onChange={(e)=>setUserName(e.target.value)} type='text' name='username'/>
                <input onChange={(e)=>setPassword(e.target.value)} type='password' name='password' />
                <input type="submit"  value="Bejelentkezés" />
            </form>
            {errorMessage &&
                <div>
                    {errorMessage}
                </div>
            }
        </div>
    );

    async function login(event: FormEvent) {
        event.preventDefault();

        const res = await api.login(userName, password);

        if (res){
            let user = await api.getUser();
            props.setUser(user);

            props.registerSocket();
        } else {
            setErrorMessage("ERROR");
        }
    }
}


