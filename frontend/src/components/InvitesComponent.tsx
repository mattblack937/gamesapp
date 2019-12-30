import React from 'react';
import {UserList} from "./UserList";
import {Chat} from "./Chat";
import {ChatMessage, Group, Invite, User} from "../util/types";
import {api} from "../util/API";


type InvitesComponentProps = {
    invites: User[],
    user?: User
}

export function InvitesComponent ( {invites, user} : InvitesComponentProps) {

    if (!user){
        return (null);
    }

    if (!invites){
        return (
            <div className={"invites"}>
                NO INVITES
            </div>
        );
    }

    return (
        <div className={"invites"}>
            <UserList caption={"YOUR INVITES:"} users={invites} onClick={(user)=> api.acceptInvite(user.id)}/>
        </div>
    );

}
