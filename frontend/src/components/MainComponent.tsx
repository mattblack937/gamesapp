import React, {ReactNode, useState} from 'react';
import {UserList} from "./UserList";
import {Chat} from "./Chat";
import {ChatMessage, Group, User} from "../util/types";
import {api} from "../util/API";
import {UsersComponent} from "./UsersComponent";


type MainComponentProps = {

}

type TabProps = {
    content: ReactNode
    id: string
}

type MenuButtonProps = {
    text: string
    id: string
}

export function MainComponent ( {} : MainComponentProps) {

    const [activeTab, setActiveTab] = useState<string>("game");

    return (
        <div className={"main-component"}>
            <Menu/>
            <Content/>
        </div>
    );

    function Menu () {
        return (
            <div className={"menu"}>
                <MenuButton id={"game"} text={"GAME"}/>
                <MenuButton id={"home"} text={"HOME"}/>
            </div>
        );
    }

    function MenuButton( {id, text}: MenuButtonProps) {
        return (
            <div className={"button"} onClick={()=> setActiveTab(id)}>
                {text}
            </div>
        );
    }

    function Content () {
        return (
            <div className={"content"}>
                <Tab id={"game"} content={
                    <div>
                        GAME
                    </div>
                }/>
                <Tab id={"home"} content={
                    <div>
                        HOME
                    </div>
                }/>
            </div>
        );
    }

    function Tab({content, id} :TabProps ) {
        if (id===activeTab){
            return(
                <div className={"tab"}>
                    {content}
                </div>
            );
        }

        return null;
    }

}
