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
    webSocket?: WebSocketEntity,
    users: User[],
    chatMessages: ChatMessage[],
    componentDidMount: boolean
}

export class App extends Component<{}, AppState> {
    constructor(props: any){
        super(props);

        this.registerSocket = this.registerSocket.bind(this);
        this.setUser = this.setUser.bind(this);

        this.sendChatMessage = this.sendChatMessage.bind(this);

        this.state = {
            chatMessages: [],
            users: [],
            componentDidMount: false
        };
    }

    async componentDidMount() {
        const user = await api.getUser();
        this.setState({
            user,
            componentDidMount: true
        });

        user && !this.state.webSocket &&
        this.registerSocket();
    }

    render(){
        if(!this.state || !this.state.componentDidMount) {
            return (<div >loading...</div>);
        }

        return (
            <div className="App">
                <header className="App-header">

                    <Logout/>

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
        const webSocket = new WebSocketEntity(this, token);
        this.setState({
            webSocket: webSocket
        });
    }

    setUser(user: User) {
        this.setState({
            user
        });
    }

    sendMessage(data: any, messageType: MessageType){
        this.state.user &&
        this.state.webSocket && this.state.webSocket.sendMessage(this.state.user, data, messageType);
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
}

export default App;
