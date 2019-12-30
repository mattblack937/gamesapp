import React from 'react';
import {UserList} from "./UserList";
import {Chat} from "./Chat";
import {ChatMessage, Group, User} from "../util/types";
import {api} from "../util/API";
import {UsersComponent} from "./UsersComponent";


type GroupComponentProps = {
    user?: User,
    group?: Group
}

export function GroupComponent ( {user, group} : GroupComponentProps) {

    if (!user){
        return (null);
    }

    if (!group){
        return (
            <div className={"group"}>
                NO GROUP
            </div>
        );
    }

    return (
        <div className={"group"}>
            <UserList caption={"GROUP OWNER:"} users={[group.owner]}/>

            <UserList caption={"GROUP MEMBERS:"} users={group.users}/>

            <div onClick={async ()=> api.leaveGroup()}>
                LEAVE GROUP
            </div>
        </div>
    );

}
