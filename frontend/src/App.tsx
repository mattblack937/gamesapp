import React, {Component} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import './css/App.css';
import {ChatMessage, User} from "./util/types";
import {Login} from "./components/Login";
import {MessageType} from "./util/enums";
import {Home} from "./components/Home";
import {api} from "./util/API";
import {WebSocketEntity} from "./util/WebSocketEntity";
import {Game} from "./components/Game";
import {Logout} from "./components/Logout";

type AppState = {
    user?: User,
    webSocketEntity?: WebSocketEntity,
    users: User[],
    chatMessages: ChatMessage[],
    componentDidMount: boolean
}

const initialState = {
    user: undefined,
    webSocketEntity: undefined,
    users: [],
    chatMessages: [],
    componentDidMount: false
} as AppState;

export class App extends Component<{}, AppState> {
    constructor(props: any){
        super(props);

        this.registerSocket = this.registerSocket.bind(this);
        this.setUser = this.setUser.bind(this);

        this.sendChatMessage = this.sendChatMessage.bind(this);

        this.state = initialState;
    }

    async componentDidMount() {
        const user = await api.getUser();
        this.setState({
            user,
            componentDidMount: true
        });

        user && !this.state.webSocketEntity &&
        this.registerSocket();

    }

    render(){
        if(!this.state || !this.state.componentDidMount) {
            return (<div >loading...</div>);
        }

        return (
            <div className="App">
                <header className="App-header">

                    {this.state.user &&
                        <Logout/>
                    }

                    <Switch>
                        {!this.state.user &&
                            <Route>
                                <Login setUser={this.setUser} registerSocket={this.registerSocket}/>
                            </Route>
                        }

                        <Route path={"/home"}>
                            <Home users={this.state.users} chatMessages={this.state.chatMessages} sendChatMessage={this.sendChatMessage}/>
                        </Route>

                        <Route path={"/game"}>
                            <Game/>
                        </Route>

                        <Redirect to="/home" />
                    </Switch>
                </header>
            </div>
        );
    }

    async registerSocket(){
        let token = await api.getUserToken();
        const webSocketEntity = new WebSocketEntity(this, token);
        this.setState({
            webSocketEntity: webSocketEntity
        });
    }

    setUser(user: User) {
        this.setState({
            user
        });
    }

    sendMessage(data: any, messageType: MessageType){
        this.state.webSocketEntity!.sendMessage(data, messageType);
    }

    setUsers(users: User[]) {
        this.setState({
            users
        });
    }

    addChatMessage(chatMessage: ChatMessage) {
        this.setState({
            chatMessages: this.state.chatMessages.concat(chatMessage)
        })
    }

    sendChatMessage(text: string) {
        this.sendMessage({message: text} as ChatMessage, MessageType.CHAT_MESSAGE);
    }

    updateState(appState: AppState) {
        this.setState(appState);
    }

    onLogout() {
        this.setState(initialState);
        this.componentDidMount();
    }
}

export default App;
