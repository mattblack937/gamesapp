import React from 'react';
import {User} from "../util/types";

type UserListProps = {
    users: User[],
}

export function UserList (props: UserListProps) {
    return (
        <div className={"user-list"}>
            <div>
                User List:
            </div>
            {props.users.map(user=>
                <div key={user.id} className={"user"}>
                    {user.name}
                </div>
            )}
        </div>
    );
}
