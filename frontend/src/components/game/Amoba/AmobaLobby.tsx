import React, {Component, useState} from 'react';
import {RouteComponentProps} from "react-router";
import {LobbyProps, } from "../Lobby";
import {UserList} from "../../UserList";
import {api} from "../../../util/API";


export class AmobaLobby extends Component<LobbyProps, {}> {
    constructor(props: any){
        super(props);
    }

    render() {
        return (
            <div>
                AMOBA LOBBY
                <UserList users={this.props.lobby!.players} onClick={(user)=>this.inviteUser(user.id) }/>
                <UserList users={this.props.users} onClick={(user)=>this.inviteUser2(user.id) }/>
                <div onClick={()=> api.getUser()}>
                    getuser
                </div>
            </div>
        );
    }

    async inviteUser(userId: string){
        console.log("INVITE", userId);
        await api.inviteUser("asd");
    }

    async inviteUser2(userId: string){
        console.log("INVITE", userId);
        await api.login("asd", "as");
    }
}
