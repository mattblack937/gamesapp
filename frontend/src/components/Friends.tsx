import React, {useContext, useEffect, useState} from 'react';
import '../css/friends.css';
import {Modal} from "./Modal";
import {ReactComponent as FriendsLogo} from '../svg/friends.svg';
import {ReactComponent as PlusLogo} from '../svg/plus2.svg';
import {ReactComponent as MinusLogo40} from '../svg/minus40.svg';
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

    const [removeFriendActive, setRemoveFriendActive] = useState<boolean>(false);

    const {friends}  = useContext(AppContext);

    const friendsLogo =
        <div className={"friends-logo"} onClick={()=> {setFriendsOpen(!friendsOpen);setAddNewFriendOpen(false);setRemoveFriendActive(false)}}>
            <FriendsLogo/>
        </div>;

    const addFriendLogo =
        <div className={"add-friend-logo"} onClick={()=> {setAddNewFriendOpen(!addNewFriendOpen);setRemoveFriendActive(false)}}>
            <PlusLogo/>
        </div>;

    const removeFriendLogo =
        <div className={"remove-friend-logo" + (removeFriendActive ? " active" : "")} onClick={()=> {setRemoveFriendActive(!removeFriendActive);setAddNewFriendOpen(false)}}>
            <MinusLogo40/>
        </div>;

    return (
        <div className={"friends"}>

            <Modal isOpen={friendsOpen} closeOnBackGroundClick={true} close={()=> {setFriendsOpen(false); setAddNewFriendOpen(false); setRemoveFriendActive(false)}}>

                <div className={"friend-list"}>
                    {friends && friends.sort((a, b) => a.name < b.name ? 1 : -1).sort((a, b) => a.userState===UserState.ONLINE ? -1 : 1).map((friend, key) =>
                        <FriendCard key={key} friend={friend} />
                    )}
                </div>

                {removeFriendLogo}
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

    async function removeFriend(friend: User) {
        await api.removeFriend(friend.id);
    }

    function FriendCard({friend}: FriendCardProps) {
        return (
            <div className={"friend-card"} onClick={()=> removeFriendActive && removeFriend(friend)}>
                <div className={"friend-name"}>
                    {friend.name}
                </div>
                <div className={"friend-state " + (friend.userState.toString())}/>
            </div>
        );
    }

    function AddNewFriendComp() {

        const [error, setError] = useState<string>("");

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
            let error = await api.requestFriend(userName);
            if(!error){
                setUserName("");
                setAddNewFriendOpen(false);
            } else {
                setError(error.message);
            }
        }

        return (
            <div className={"add-new-friend-wrapper"}>
                <div className={"add-new-friend" + (isFilled() ? " filled" : "")}  onClick={()=> isFilled() && addNewFriend(userNameRef.current)}>
                    <div className={"input-with-default-value user-name"}>
                        <input value={userName} required={true} autoFocus={true} onClick={(e)=> e.stopPropagation()} type='text' onChange={(e)=>setUserName(e.target.value)}/>
                        <div></div>
                    </div>
                    <div className={"plus-sign"}>
                        <PlusLogo70/>
                    </div>
                </div>

                {error &&
                <div className={"error"} onClick={()=> setError("")}>
                    {error}
                </div>
                }
            </div>

        );
    }



}

















