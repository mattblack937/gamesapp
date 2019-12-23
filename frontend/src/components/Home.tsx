import React from 'react';
import {UserList} from "./UserList";
import {Chat} from "./Chat";
import {ChatMessage, User} from "../util/types";

type HomeType = {
    users: User[],
    chatMessages: ChatMessage[],
    sendChatMessage: (text: string) => void
}

export function Home (props: HomeType) {

    return (
        <div >
            {/*<UserList users={props.users}/>*/}
            {/*<Chat chatMessages={props.chatMessages} sendChatMessage={props.sendChatMessage}/>*/}
        </div>
    );
}
