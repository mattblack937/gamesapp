import React, {FormEvent, useState} from 'react';
import {User} from "../util/types";
import {api} from "../util/API";
import {func} from "prop-types";

import {App, AppContext} from "../App";

type LoginProps = {
    refresh:  () => void
}

export function Login (props: LoginProps) {

    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState();

    return (
        <AppContext.Consumer>{
            ({authenticated, lang})=>{
                return(
                    <div className={"login" + (filled() ? " active" : "")}
                         onClick={()=> filled() && login()}>
                        <input autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' name='username' onChange={(e)=> {
                            setUserName(e.target.value);
                        }}/>
                        <input onClick={(e)=> e.stopPropagation()} type='password' name='password' onChange={(e)=>{
                            setPassword(e.target.value);
                        }}/>
                        <div>Login</div>
                    </div>
                )
            }
        }</AppContext.Consumer>


    );

    function filled(){
        return userName!="" && password!="";
    }

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


