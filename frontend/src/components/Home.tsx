import React from 'react';
import {UserList} from "./UserList";
import {Chat} from "./Chat";
import {ChatMessage, Group, User} from "../util/types";
import {GroupComponent} from "./GroupComponent";
import {UsersComponent} from "./UsersComponent";
import {InvitesComponent} from "./InvitesComponent";
import {GameSettingsComponent} from "./GameSettingsComponent";

type HomeProps = {
    group?: Group,
    user: User,
    users: User[],
    invites: User[]
}

export function Home ({group, user, users, invites}: HomeProps) {

    return (
        <div >
            <GameSettingsComponent/>
            <GroupComponent group={group} user={user}/>
            <UsersComponent users={users} user={user}/>
            <InvitesComponent invites={invites} user={user}/>
        </div>
    );
}
