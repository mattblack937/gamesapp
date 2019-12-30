import React from 'react';
import {UserList} from "./UserList";
import {Chat} from "./Chat";
import {ChatMessage, Group, User} from "../util/types";
import {api} from "../util/API";
import {UsersComponent} from "./UsersComponent";


type GroupChatProps = {
    messages: ChatMessage[]
}

export function GroupChat ( {messages} : GroupChatProps) {

    return (
        <div className={"group-chat"}>
            <Chat sendChatMessage={async (message)=> await api.sendGroupMessage(message)} chatMessages={messages}/>
        </div>
    );

}
