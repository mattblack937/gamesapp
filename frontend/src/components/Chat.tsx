import React, {useContext, useEffect, useState} from 'react';
import {ChatMessage} from "../util/types";
import {AppContext} from "../App";
import { ReactComponent as SendLogo } from '../svg/send.svg';
import {Key} from "ts-keycode-enum";
import {api} from "../util/API";


export function Chat () {

    const [text, _setText] = useState("");
    const { chatMessages, user, } = useContext(AppContext);

    const textRef = React.useRef(text);
    const setText = (text: string) => {
        textRef.current = text;
        _setText(text);
    };

    const listener = (event: KeyboardEvent) => {
        console.log("text:", textRef.current);
        if (event.keyCode === Key.Enter && textRef.current != "") {
            api.sendChatMessage(textRef.current);
            setText("");
        }
    };

    useEffect(() => {
        window.addEventListener('keyup', listener);
        return () => {
            window.removeEventListener('keyup', listener);
        };
    }, []);

    return (
        <div className={"chat"}>
            <div className={"messages"}>
                {chatMessages!.map((chatMessage, key)=>
                    <div key={key} className={"message-box" + (user!.id === chatMessage.user.id ? " mine" : "")}>
                        <div className={"message-user"}>
                            {chatMessage.user.name}
                        </div>
                        <div className={"message-text"}>
                            {chatMessage.message}
                        </div>
                    </div>
                )}
            </div>
            <div className={"user-input"}>
                <input value={text} onChange={(event) => setText(event.target.value)}/>
            </div>
        }

        </div>
    );

}



