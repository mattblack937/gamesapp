import React, {Component} from 'react';
import {Link, Redirect, Route, RouteComponentProps, Switch} from "react-router-dom";
import './css/App.css';
import {ChatMessage, Game, Group, Invite, LobbyType, User} from "./util/types";
import {Login} from "./components/Login";
import {GameState, GameType, MessageType} from "./util/enums";
import {Home} from "./components/Home";
import {api} from "./util/API";
import {WebSocketEntity} from "./util/WebSocketEntity";
import {Logout} from "./components/Logout";
import {GroupComponent} from "./components/GroupComponent";
import {UsersComponent} from "./components/UsersComponent";
import {InvitesComponent} from "./components/InvitesComponent";
import {GameComponent} from "./components/GameComponent";

export type AppState = {
    user?: User,
    webSocketEntity?: WebSocketEntity,
    users: User[],
    chatMessages: ChatMessage[],
    componentDidMount: boolean,
    lobby?: LobbyType,
    group?: Group,
    invites: User[],
    game?: Game
}



const initialState = {
    user: undefined,
    webSocketEntity: undefined,
    users: [],
    chatMessages: [],
    componentDidMount: false,
    lobby: undefined,
    group: undefined,
    invites: [],
    game: undefined
} as AppState;

export class App extends Component<RouteComponentProps<{}>, AppState> {
    constructor(props: any){
        super(props);

        this.registerSocket = this.registerSocket.bind(this);
        this.setUser = this.setUser.bind(this);
        this.sendChatMessage = this.sendChatMessage.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);

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




                    {/*<div>*/}
                    {/*    <div onClick={ ()=> api.post1()}>*/}
                    {/*        POST1*/}
                    {/*    </div>*/}

                    {/*    <div onClick={ ()=> api.post2()}>*/}
                    {/*        POST2*/}
                    {/*    </div>*/}

                    {/*    <div onClick={ ()=> api.get1()}>*/}
                    {/*        GET1*/}
                    {/*    </div>*/}

                    {/*    <div onClick={ ()=> api.get2()}>*/}
                    {/*        Get2*/}
                    {/*    </div>*/}

                    {/*</div>*/}



                    {/*<Menu user={this.state.user} />*/}

                    <Switch>
                        {!this.state.user &&
                            <Route>
                                <Login refresh={this.componentDidMount}/>
                            </Route>
                        }

                        {this.state.game &&
                            <>
                                <Route path={"/game"}>
                                    <GameComponent user={this.state.user!} game={this.state.game}/>
                                </Route>
                                <Route >
                                    <Redirect to={"/game"}/>
                                </Route>
                            </>
                        }

                        <Route path={"/home"}>
                            <Home user={this.state.user!} users={this.state.users} invites={this.state.invites} group={this.state.group}/>
                        </Route>

                        <Redirect to="/home" />
                    </Switch>
                </header>
            </div>
        );
    }

    async registerSocket(){
        let token = await api.getUserToken();
        if (token){
            let webSocketEntity = new WebSocketEntity(this, token);
            this.setState({
                webSocketEntity: webSocketEntity
            });
        }
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

    addInvite(fromUser: User) {
        if (!!this.state.invites.find(invite => invite.id === fromUser.id)){
            return;
        }

        this.setState({
            invites: this.state.invites.concat(fromUser)
        })
    }

    sendChatMessage(text: string) {
        this.sendMessage({message: text} as ChatMessage, MessageType.CHAT_MESSAGE);
    }

    updateState(appState: AppState) {
        this.setState({...appState});
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
