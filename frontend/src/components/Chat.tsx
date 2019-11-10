import React, {useState} from 'react';
import {ChatMessage} from "../util/types";

type ChatProps = {
    chatMessages: ChatMessage[],
    sendChatMessage: (text: string) => void
}

export function Chat (props: ChatProps) {

    const [text, setText] = useState("");

    return (
        <div className={"chat"}>
            <div>
                Chat:
            </div>
            {props.chatMessages.map((chatMessages, key)=>
                <div key={key} className={"message"}>
                    {chatMessages.user.name} : {chatMessages.message}
                </div>
            )}
            <input value={text} onChange={(event) => setText(event.target.value)}/>
        
            <button onClick={()=>
                {
                    props.sendChatMessage(text);
                    setText("");
                }}>KÜLDÉS</button>
        </div>
    );
}



