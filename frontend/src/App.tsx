import React, {Component} from 'react';
import {Link, Redirect, Route, RouteComponentProps, Switch} from "react-router-dom";
import './css/App.css';
import {ChatMessage, User} from "./util/types";
import {Login} from "./components/Login";
import {GameState, GameType, MessageType} from "./util/enums";
import {Home} from "./components/Home";
import {api} from "./util/API";
import {WebSocketEntity} from "./util/WebSocketEntity";
import {Game} from "./components/game/Game";
import {Logout} from "./components/Logout";
import {Lobby} from "./components/game/Lobby";

type AppState = {
    user?: User,
    webSocketEntity?: WebSocketEntity,
    users: User[],
    chatMessages: ChatMessage[],
    componentDidMount: boolean,
    gameState: GameState,
    gameType?: GameType
}

enum MenuEnum {
    HOME="home", GAME="game", LOBBY="lobby", PEOPLE="people"
}

const initialState = {
    user: undefined,
    webSocketEntity: undefined,
    users: [],
    chatMessages: [],
    componentDidMount: false,
    activeMenu: MenuEnum.HOME,
    gameState: GameState.BROWSING,
    gameType: undefined
} as AppState;

export class App extends Component<RouteComponentProps<{}>, AppState> {
    constructor(props: any){
        super(props);

        this.registerSocket = this.registerSocket.bind(this);
        this.setUser = this.setUser.bind(this);
        this.sendChatMessage = this.sendChatMessage.bind(this);

        this.state = initialState;
    }

    async componentDidMount() {

        const user = await api.getUser();
        await user && !this.state.webSocketEntity && this.registerSocket();

        this.setState({
            user,
            componentDidMount: true
        });

    }

    render(){
        if(!this.state || !this.state.componentDidMount) {
            return (<div >loading...</div>);
        }

        return (
            <div className="App">

                {this.state.user &&
                    <Logout/>
                }

                <header className="App-header">

                    <Menu user={this.state.user} />

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

                        <Route path={"/lobby"}>
                            <Lobby gameState={this.state.gameState} gameType={this.state.gameType}/>
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
        });
    }

    sendChatMessage(text: string) {
        this.sendMessage({message: text} as ChatMessage, MessageType.CHAT_MESSAGE);
    }

    updateState(appState: AppState) {
        this.setState(appState);
    }

    async onLogout() {
        this.setState(initialState, () => {
            this.componentDidMount();
        });
    }
}

type MenuProps = {
    user?: User
}

function Menu({ user }: MenuProps){

    if (!user){
        return (null);
    }

    return (
        <div className={"menu-container"}>
            <div className={"menu-element"}>
                <Link to={"/home"}>
                    HOME
                </Link>
            </div>
            <div className={"menu-element"}>
                <Link to={"/lobby"}>
                    LOBBY
                </Link>
            </div>
        </div>
    );
}

export default App;
