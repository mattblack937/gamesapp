import React from 'react';
import {User} from "../util/types";

type UserListProps = {
    users: User[],
    onClick?: (user: User) => void
}

export function UserList (props: UserListProps) {
    return (
        <div className={"user-list"}>
            <div>
                User List:
            </div>
            {props.users.map(user=>
                <div key={user.id} className={"user"} onClick={()=>{ props.onClick && props.onClick(user)}}>
                    {user.name}
                </div>
            )}
        </div>
    );
}
