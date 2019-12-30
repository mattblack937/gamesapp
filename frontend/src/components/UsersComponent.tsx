import React from 'react';
import {UserList} from "./UserList";
import {Chat} from "./Chat";
import {ChatMessage, Group, User} from "../util/types";
import {api} from "../util/API";


type UsersComponentProps = {
    user?: User,
    users: User[]
}

export function UsersComponent ( {user, users} : UsersComponentProps) {

    if (!user){
        return (null);
    }

    return (
        <div className={"users"}>
            <UserList caption={"YOU:"} users={[user]}/>

            <UserList caption={"ALL USERS:"} users={users} onClick={(user)=> api.inviteUser(user.id)}/>
        </div>
    );

}
