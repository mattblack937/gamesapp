import React, {useEffect, useState} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import './css/app.css';

import {ChatMessage, User, WSMessage} from "./util/types";
import {Login} from "./components/Login";
import {MessageType} from "./util/enums";
import {api} from "./util/API";

import {Header} from "./components/Header";
import {Chat} from "./components/Chat";

export const AppContext = React.createContext<Partial<ContextProps>>({});

export const LOG_ON = true;


export type ContextProps = {
    user: User | null,
    reconnect: () => void,
    chatMessages: ChatMessage[]
};

export function App () {
    const [user, setUser] = useState<User | null | undefined>(undefined);
    const [webSocket, setWebsocket] = useState<WebSocket | null>(null);

    const [chatMessages, _setChatMessages] = useState<ChatMessage[]>([]);
    const chatMessagesRef = React.useRef(chatMessages);
    const setChatMessages = (chatMessages: ChatMessage[]) => {
        chatMessagesRef.current = chatMessages;
        _setChatMessages(chatMessages);
    };

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

    if(user === undefined){
        return null;
    }

    return (
        <AppContext.Provider
            value={{
                user,
                reconnect,
                chatMessages
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
                        <Chat />
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

        setChatMessages([]);

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
                            setChatMessages(chatMessagesRef.current.concat(JSON.parse(message.data) as ChatMessage));
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


}

export default App;
