import React, {useState} from 'react';
import '../css/friends.css';
import {Modal} from "./Modal";
import {ReactComponent as FriendsLogo} from '../svg/friends.svg';
import {User, UserState} from "../util/types";
import {type} from "os";

export function Friends () {

    const [isOpen, setIsOpen] = useState<boolean>(false);

    const [friends, setFriends] = useState<User[]>([
        {
            name: "ROBI",
            id: "1",
            state: UserState.ONLINE
        } as User,
        {
            id: "2",
            name: "MARCI MARCI MARCI MARCI",
            state: UserState.OFFLINE
        } as User,
        {
            id: "3",
            name: "MARCIMARCIMARCIMARCI",
            state: UserState.OFFLINE
        } as User,
        {
            name: "ROBI",
            id: "1",
            state: UserState.ONLINE
        } as User,
        {
            id: "2",
            name: "I I I I I I I I I I I I I I I I I I I I I I I I I I I I I I I I I I",
            state: UserState.OFFLINE
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
        } as User,{
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
        } as User,{
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
        } as User,{
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
        } as User,{
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
        } as User,{
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
        } as User,{
            id: "3",
            name: "MARCIMARCIMARCIMARCI",
            state: UserState.OFFLINE
        } as User,{
            id: "3",
            name: "MARCIMARCIMARCIMARCI",
            state: UserState.OFFLINE
        } as User,
        {
            name: "ROBI",
            id: "1",
            state: UserState.ONLINE
        } as User,
        {
            id: "2",
            name: "MARCI MARCI MARCI MARCI",
            state: UserState.OFFLINE
        } as User,
        {
            id: "3",
            name: "MARCIMARCIMARCIMARCI",
            state: UserState.OFFLINE
        } as User
    ]);

    const logo =
        <div className={"friends-logo"} onClick={()=> setIsOpen(!isOpen)}>
            <FriendsLogo/>
        </div>;

    return (
        <div className={"friends"}>
            {isOpen ?
                <Modal isOpen={isOpen} closeOnBackGroundClick={true} close={()=> setIsOpen(false)}>
                    <div className={"friend-list"}>
                        {friends.sort((a, b) => a.state===UserState.ONLINE ? -1 : 1).map((friend, key) =>
                            <FriendCard key={key} friend={friend} />
                        )}
                    </div>
                    {logo}
                </Modal>:
                logo
            }
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

}

















