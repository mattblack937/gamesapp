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
    user: User | null;
};

type AppState = {
    authenticated: boolean,
    user?: User,
    webSocket: WebSocket| null,
    users: User[],
    chatMessages: ChatMessage[],
    componentDidMount: boolean,
    lobby?: LobbyType,
    group?: Group,
    invites: User[],
    game?: Game
}

export function App () {

    const [appState, setAppState] = useState<AppState>(getInitialAppState());

    useEffect(  () => {
        async function asyncF(){
            console.log("useEffect 1");
            if(appState.user === undefined){
                await getUser();
            }
        }asyncF()}, [appState.user]);

    useEffect(  () => {
        async function asyncF(){
            console.log("useEffect 2");
            setAppState({...appState,
                authenticated: appState.user != null
            })
        }asyncF()}, [appState.user]);

    useEffect(  () => {
        async function asyncF(){
            console.log("useEffect 3");
            await reConnectWebSocket();
            console.log("useEffect 3" , appState.webSocket);
        }asyncF()}, [appState.authenticated]);




    return (
        <AppContext.Provider value={{
            login: (userName, password)=> login(userName, password),
            logout: ()=> logout(),
            user: appState.user
        }}>
            <div className="App">

                {"authenticated: "+appState.authenticated}

                {"  user: "+ (appState.user && appState.user.name)}

                {"   webSocket: "+appState.webSocket}

                {appState.authenticated && <Logout/>}
                <header className="App-header">
                    <Switch>
                        { !appState.authenticated &&
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

    async function getUser(){
        let user = await api.getUser();
        console.log("getUser: " + (user && user.name));
        setAppState({ ...appState,
            user: user
        });
    }

    function sendMessage(data: any, messageType: MessageType){
        let messageDto = JSON.stringify({
                data: JSON.stringify(data),
                messageType: messageType
        } as WSMessage);
        console.log("sendMessage: ", appState.webSocket, messageDto )
        appState.webSocket && appState.webSocket.send(messageDto);
    }


    async function reConnectWebSocket(){
        console.log("reConnectWebSocket", appState.webSocket);
        appState.webSocket && appState.webSocket.close();

        console.log("reConnectWebSocket.appState.user: " + appState.user);
        if(!appState.user){
            await setAppState({...appState,
                webSocket: null
            });
            return;
        }

        let token = await api.getUserToken();

        let webSocket = new WebSocket("ws://localhost:9000") as WebSocket;

        console.log("reConnectWebSocket.webSocket: " + webSocket);

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
                console.log("onerror");
                appState.webSocket && appState.webSocket.close();
                setAppState(getInitialAppState);
            };

            //////////////////////////////

            webSocket.onclose = (response) => {
                console.log("onclose");
                setAppState(getInitialAppState);
            };

            //////////////////////////////

            webSocket.onopen = (response) => {
                console.log("onOpen");
                let messageDto = JSON.stringify({
                    data: JSON.stringify({
                        userId: appState.user!.id,
                        token
                    }),
                    messageType: MessageType.USER_TOKEN
                } as WSMessage);
                console.log("onOpen: ", webSocket, appState.webSocket, messageDto);
                webSocket && webSocket.send(messageDto);
            };

            //////////////////////////////
        }

        console.log("reConnectWebSocket.appState1:", appState);

        console.log("reConnectWebSocket.appState1:", webSocket);

        await setAppState({...appState,
            webSocket});

        console.log("reConnectWebSocket.appState2:", appState);
    }

    async function login(userName: string, password: string) {
        console.log("login");
        await api.login(userName, password);
        await getUser();
    }

    async function logout() {
        console.log("logout1")
        appState.webSocket && await appState.webSocket.close();
        console.log("logout2")
        await api.logout();
        console.log("logout3")
        await setAppState(getInitialAppState());
        console.log("logout4")
    }

    function getInitialAppState() : AppState {
        return ({
            authenticated: false,
            user: undefined,
            webSocket: null,
            chatMessages: [],
            componentDidMount: false,
            game: undefined,
            group: undefined,
            invites: [],
            lobby: undefined,
            users: []
        });
    }

}

export default App;
