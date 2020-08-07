import React, {useEffect, useState} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import './css/app.css';

import {ChatMessage, Game, Group, LobbyType, User, WSMessage} from "./util/types";
import {Login} from "./components/Login";
import {MessageType} from "./util/enums";
import {api} from "./util/API";

import {MainComponent} from "./components/MainComponent";
import {async} from "q";
import {func} from "prop-types";
import {Header} from "./components/Header";
import {Key} from "ts-keycode-enum";

export const AppContext = React.createContext<Partial<ContextProps>>({});

export const LOG_ON = true;


export type ContextProps = {
    login: (userName: string, password: string)=>{}
    logout: ()=> {}
    user: User | null,
    reconnect: () => void
};

export function App () {
    const [user, setUser] = useState<User | null | undefined>(undefined);
    const [webSocket, setWebsocket] = useState<WebSocket | null>(null);
    const authenticated = !! user;

    useEffect(() => {
        LOG_ON && console.log("HERE WE GO!");
    }, []);

    useEffect(() => {
        LOG_ON && console.log("useEffect START");
        if(user === undefined){
            setInitialState();
            fetchUser();
        } else {
            createNewWebSocket(user);
        }
        LOG_ON && console.log("useEffect END");
    }, [user]);


    return (
        <AppContext.Provider
            value={{
                login,
                logout,
                user,
                reconnect
            }}>


            <div className="app">
                <Switch>

                    { !user &&
                    <Route>

                        <Login/>
                    </Route>
                    }

                    <Route exact={true} path={"/"} >
                        <Header />
                        {/*<MainComponent />*/}
                    </Route>

                    <Redirect to="" />
                </Switch>
            </div>
        </AppContext.Provider>
    );

    async function fetchUser(){
        LOG_ON && console.log("fetchUser START");
        let user = await api.getUser();
        setUser(user);
        LOG_ON && console.log("fetchUser END");
    }

    function reconnect() {
        setUser(undefined);
    }

    function sendMessage(data: any, messageType: MessageType){
        LOG_ON && console.log("sendMessage: ",messageType, data);
        let messageDto = JSON.stringify({
                data: JSON.stringify(data),
                messageType: messageType
        } as WSMessage);
        webSocket && webSocket.send(messageDto);
    }

    function setInitialState() {
        LOG_ON && console.log("setInitialState START");
        setUser(undefined);
        webSocket && webSocket.close();
        setWebsocket(null);
        LOG_ON && console.log("setInitialState END");
    }

    async function createNewWebSocket(user: User | null){
        LOG_ON && console.log("createNewWebSocket START");

        webSocket && webSocket.close();

        if(!user){
            setWebsocket(null);
            LOG_ON && console.log("createNewWebSocket END");
            return;
        } else {
            let token = await api.getUserToken();
            let webSocket = new WebSocket("ws://localhost:9000") as WebSocket;

            {
                //////////////////////////////
                webSocket.onmessage = (json) => {
                    let message = JSON.parse(json.data) as WSMessage;
                    LOG_ON && console.log("onmessage: ", message);
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
                    LOG_ON && console.log("onError START");
                    setUser(undefined);
                    LOG_ON && console.log("onError END");
                };

                //////////////////////////////

                webSocket.onclose = (response) => {
                    LOG_ON && console.log("onclose START");
                    setUser(undefined);
                    LOG_ON && console.log("onclose END");
                };

                //////////////////////////////

                webSocket.onopen = async (response) => {
                    LOG_ON && console.log("onOpen START");

                    let messageDto = JSON.stringify({
                        data: JSON.stringify({
                            userId: user.id,
                            token
                        }),
                        messageType: MessageType.USER_TOKEN
                    } as WSMessage);
                    webSocket.send(messageDto);
                    LOG_ON && console.log("onOpen END");
                };

                //////////////////////////////
            }

            setWebsocket(webSocket);
        }

        LOG_ON && console.log("createNewWebSocket END");
    }

    async function login(userName: string, password: string) {
        LOG_ON && console.log("login START");
        await api.login(userName, password);
        fetchUser();
        LOG_ON && console.log("login END");
    }

    async function logout() {
        LOG_ON && console.log("logout START");
        await api.logout();
        fetchUser();
        LOG_ON && console.log("logout END");
    }

}

export default App;
