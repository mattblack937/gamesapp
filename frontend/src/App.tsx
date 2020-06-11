import React, {useEffect, useState} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import './css/App.css';
import {ChatMessage, Game, Group, LobbyType, User, WSMessage} from "./util/types";
import {Login} from "./components/Login";
import {MessageType} from "./util/enums";
import {api} from "./util/API";
import {Logout} from "./components/Logout";
import {MainComponent} from "./components/MainComponent";
import {async} from "q";
import {func} from "prop-types";

export const AppContext = React.createContext<Partial<ContextProps>>({});

export type ContextProps = {
    login: (userName: string, password: string)=>{}
    logout: ()=>{}
    user: User | null
    authenticated: boolean
};

export function App () {
    const [user, setUser] = useState<User | null | undefined>(undefined);
    const [webSocket, setWebsocket] = useState<WebSocket | null>(null);
    const authenticated = !! user;

    useEffect(() => {
        console.log("useEffect0");
    }, []);

    useEffect(() => {
        async function f(){
            console.log("useEffect1");
            if(user === undefined){
                let u = await fetchUser();
            } else {
                createNewWebSocket(user);
            }
        }
        f();
    }, [user]);

    return (
        <AppContext.Provider value={{
            login,
            logout,
            user,
            authenticated
        }}>
            <div className="App">

                {"authenticated: " + authenticated}

                {"  user: "+ (user && user.name)}

                {"   webSocket: "+webSocket}

                {authenticated && <Logout/>}
                <header className="App-header">
                    <Switch>
                        { !authenticated &&
                        <Route>
                            <Login/>
                        </Route>
                        }

                        <Route exact={true} path={"/"} >
                            hello
                            <MainComponent />
                        </Route>

                        <Redirect to="" />
                    </Switch>
                </header>
            </div>
        </AppContext.Provider>
    );

    async function fetchUser(){
        console.log("fetchUser");
        let user = await api.getUser();
        setUser(user);
        return user;
    }

    function sendMessage(data: any, messageType: MessageType){
        let messageDto = JSON.stringify({
                data: JSON.stringify(data),
                messageType: messageType
        } as WSMessage);
        // console.log(messageDto)
        webSocket && webSocket.send(messageDto);
    }


    function setInitialState() {
        console.log("setInitialState");
        setUser(undefined);
        setWebsocket(null);
    }

    async function createNewWebSocket(user: User | null){
        console.log("createNewWebSocket");
        webSocket && webSocket.close();

        if(!user){
            setWebsocket(null);
            return;
        } else {

            let webSocket = new WebSocket("ws://localhost:9000") as WebSocket;
            {
                //////////////////////////////

                webSocket.onmessage = (json) => {
                    console.log(json);
                    let message = JSON.parse(json.data) as WSMessage;
                    console.log(message);
                    switch (message.messageType) {
                        case MessageType.STATE.valueOf():
                            // app.updateState(JSON.parse(message.data) as AppState);
                            break;
                        case MessageType.CHAT_MESSAGE.valueOf():
                            // app.addChatMessage(JSON.parse(message.data) as ChatMessage);
                            break;
                        case MessageType.INVITE.valueOf():
                            // app.addInvite(JSON.parse(message.data) as User);
                            break;
                    }
                };

                //////////////////////////////

                webSocket.onerror = (response) => {
                    console.log("onError");
                    webSocket && webSocket.close();
                    setInitialState();
                };

                //////////////////////////////

                webSocket.onclose = (response) => {
                    console.log("onclose");
                    webSocket && webSocket.close();
                    setInitialState()
                };

                //////////////////////////////

                webSocket.onopen = async (response) => {
                    console.log("onOpen");
                    let token = await api.getUserToken();
                    let messageDto = JSON.stringify({
                        data: JSON.stringify({
                            userId: user.id,
                            token
                        }),
                        messageType: MessageType.USER_TOKEN
                    } as WSMessage);
                    webSocket.send(messageDto);
                };

                //////////////////////////////
            }

            setWebsocket(webSocket);
        }
    }

    async function login(userName: string, password: string) {
        console.log("login");
        await api.login(userName, password);
        fetchUser();
    }

    async function logout() {
        console.log("logout");
        webSocket && webSocket.close();
        await api.logout();
        setInitialState();
    }

}

export default App;
