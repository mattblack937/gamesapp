import React, {useEffect, useState} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import './css/App.css';
import {ChatMessage, Game, Group, LobbyType, User, WSMessage} from "./util/types";
import {Login} from "./components/Login";
import {MessageType} from "./util/enums";
import {api} from "./util/API";
import {Logout} from "./components/Logout";
import {MainComponent} from "./components/MainComponent";

export const AppContext = React.createContext<Partial<ContextProps>>({});
let webSocket: null | WebSocket = null;

export type ContextProps = {
    login: (userName: string, password: string)=>{}
    logout: ()=>{}
    user: User
};

type AppState = {
    user?: User,
    users: User[],
    chatMessages: ChatMessage[],
    componentDidMount: boolean,
    lobby?: LobbyType,
    group?: Group,
    invites: User[],
    game?: Game,
    webSocket?: WebSocket
}

export function App () {

    const [appState, setAppState] = useState<AppState>(getInitialAppState());
    useEffect( () => {
        getUser()
    }, []);
    useEffect( () => {
        if(appState.user && !appState.webSocket){
            registerWebSocket()
        }
    }, [appState.user]);

    return (
        <AppContext.Provider value={{
            login: (userName, password)=> login(userName, password),
            logout: ()=> logout(),
            user: appState.user
        }}>
            <div className="App">
                {appState.user && <Logout/>}
                <header className="App-header">
                    <Switch>
                        { !appState.user &&
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

    async function login(userName: string, password: string) {
        await api.login(userName, password);
        await getUser();
    }

    async function getUser(){
        setAppState({ ...appState,
            user: await api.getUser()
        });
    }

    function sendMessage(data: any, messageType: MessageType){
        let messageDto = JSON.stringify({
                data: JSON.stringify(data),
                messageType: messageType
        } as WSMessage);
        webSocket && webSocket.send(messageDto);
    }


    async function registerWebSocket(){
        webSocket && webSocket.close();
        let token = await api.getUserToken();
        webSocket = new WebSocket("ws://localhost:9000");

        webSocket.onopen = async (response) => {
            sendMessage({
                userId: appState.user!.id,
                token
            }, MessageType.USER_TOKEN)
        };

        webSocket.onerror = (response) => {
            setAppState(getInitialAppState);
        };

        webSocket.onclose = (response) => {
            setAppState(getInitialAppState);
        };

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
    }

    async function logout() {
        await api.logout();
        appState.webSocket && appState.webSocket.close();
        setAppState(getInitialAppState());
    }

    function getInitialAppState() {
        return ({
            user: undefined,
            chatMessages: [],
            componentDidMount: false,
            game: undefined,
            group: undefined,
            invites: [],
            lobby: undefined,
            users: [],
            webSocketEntity: undefined,
            webSocket: undefined
        } as AppState);
    }

}

export default App;
