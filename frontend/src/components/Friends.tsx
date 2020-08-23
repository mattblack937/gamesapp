import React, {useContext, useEffect, useState} from 'react';
import '../css/friends.css';
import {Modal} from "./Modal";
import {ReactComponent as FriendsLogo} from '../svg/friends.svg';
import {ReactComponent as PlusLogo} from '../svg/plus2.svg';
import {ReactComponent as PlusLogo70} from '../svg/plus70.svg';
import {User, UserState} from "../util/types";
import {type} from "os";
import {Key} from "ts-keycode-enum";
import {async} from "q";
import {AppContext} from "../App";
import {api} from "../util/API";

export function Friends () {

    const [friendsOpen, setFriendsOpen] = useState<boolean>(false);

    const [addNewFriendOpen, setAddNewFriendOpen] = useState<boolean>(false);

    const [friends, setFriends] = useState<User[]>([
        {
            name: "ROBI",
            id: "1",
            state: UserState.ONLINE
        } as User,
        {
            id: "3",
            name: "MARCIMARCIMARCIMARCI",
            state: UserState.OFFLINE
        } as User,{
            id: "3",
            name: "MARCIMARCIMARCIMARCI",
            state: UserState.OFFLINE
        } as User,{
            id: "3",
            name: "MARCIMARCIMARCIMARCI",
            state: UserState.OFFLINE
        }
    ]);

    const friendsLogo =
        <div className={"friends-logo"} onClick={()=> {setFriendsOpen(!friendsOpen);setAddNewFriendOpen(false)}}>
            <FriendsLogo/>
        </div>;

    const addFriendLogo =
        <div className={"add-friend-logo"} onClick={()=> setAddNewFriendOpen(!addNewFriendOpen)}>
            <PlusLogo/>
        </div>;

    return (
        <div className={"friends"}>

            <Modal isOpen={friendsOpen} closeOnBackGroundClick={true} close={()=> {setFriendsOpen(false); setAddNewFriendOpen(false)}}>

                <div className={"friend-list"}>
                    {friends.sort((a, b) => a.state===UserState.ONLINE ? -1 : 1).map((friend, key) =>
                        <FriendCard key={key} friend={friend} />
                    )}
                </div>

                {addFriendLogo}
                {friendsLogo}

                {addNewFriendOpen && <AddNewFriendComp/>}

            </Modal>

            {friendsOpen || friendsLogo}

        </div>
    );

    type FriendCardProps = {
        friend: User
    }

    function FriendCard({friend}: FriendCardProps) {
        return (
            <div className={"friend-card"}>
                <div className={"friend-name"}>
                    {friend.name}
                </div>
                <div className={"friend-state " + (friend.state.toString())}/>
            </div>
        );
    }

    function AddNewFriendComp() {


        function isFilled(){
            return userNameRef.current != "";
        }



        const [userName, _setUserName] = useState("");
        const userNameRef = React.useRef(userName);
        const setUserName = (userName: string) => {
            userNameRef.current = userName;
            _setUserName(userName);
        };

        async function addNewFriend(userName: string) {
            // TODO
            setUserName("");
            setAddNewFriendOpen(false);
            setFriendsOpen(false);
        }


        return (
            <div className={"add-new-friend" + (isFilled() ? " filled" : "")}>
                <div className={"input-with-default-value user-name"}>
                    <input value={userName} required={true} autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' onChange={(e)=>setUserName(e.target.value)}/>
                    <div></div>
                </div>
                <div className={"plus-sign"} onClick={()=> isFilled() && addNewFriend(userNameRef.current)}>
                    <PlusLogo70/>
                </div>
            </div>
        );
    }



}

















