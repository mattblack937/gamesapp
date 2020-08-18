import { User } from "./types";
import {GameType} from "./enums";
import {LOG_ON} from "../App";

const URL = "http://localhost:8080";

class API{

    public async createLobby(gameType: GameType) {
        let message = await fetch(URL + "/create-lobby/"+gameType, {
            method: 'GET',
            credentials: 'include'
        });
    }

    public async getUserToken(): Promise<string>{
        LOG_ON && console.log("getUserToken");
        let message = await fetch(URL + "/userToken", {
            method: 'GET',
            credentials: 'include'
        })
            .then( res => res.json())
            .then( res => { return res });
        let token = JSON.parse(message.data) as string;
        return token;
    }

    public async getUser(): Promise<User>{
        let res = await fetch(URL + '/user', {
            method: 'GET',
            credentials: 'include'
        }).then( res => res.json()).then( res =>{return res}).catch((error=>{return null}));
        LOG_ON && console.log("api.getUser" + res);
        return res as User;
    }

    public async logout() {
        let res = await fetch(URL + '/logout', {
            method: 'GET',
            credentials: 'include'
        }).then( res => res.json())
            .then( res =>{return res})
            .catch((error=>{return null}));
    }

    public async login(userName: string, password: string): Promise<boolean>{

        const res = await fetch(URL + '/login', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                    userName: userName,
                    password: password
                }
            )
        }).then( res =>{return res.ok });

        return res;
    }

    public async createNewAccount(userName: string, password: string) :Promise<Error | undefined> {

        const res = await fetch(URL + '/create-account', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                    userName: userName,
                    password: password
                }
            )
        }).then(function(response) {
            if (response.ok) {
                return;
            }
            console.log("response.status:", response.status);
            console.log("response.statusText:", response.statusText);
            return Error("UserName already taken");

        }).then(function(response) {
            console.log("response", response);
            return response;
        })

        return res;
    }

    public async inviteUser(userId: string) {
        LOG_ON && console.log("invite2:",userId);
        await fetch(URL + '/invite/' + userId, {
            method: 'GET',
            credentials: 'include'
        }).then( res => res.json())
            .then( res =>{return res})
            .catch((error=>{return null}));
    }

    public async acceptInvite(userId: string) {
        await fetch(URL + '/accept-invite/' + userId, {
            method: 'GET',
            credentials: 'include'
        }).then( res => res.json())
            .then( res =>{return res})
            .catch((error=>{return null}));
    }





    public async get1() {
        let res = await fetch(URL + '/get1', {
            method: 'GET',
            credentials: 'include'
        })
            .then( res => res.json())
            .then( res =>{return res})
            .catch((error=>{return null}));
    }

    public async get2() {
        let res = await fetch(URL + '/get2', {
            method: 'GET',
            credentials: 'include'
        })
            .then( res => res.json())
            .then( res =>{return res})
            .catch((error=>{return null}));
    }

    public async sendChatMessage(text: string) {
        console.log("api.sendChatMessage:",text);

        const res = await fetch(URL + '/chat-message', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: text
        }).then( res =>{return res.ok });

        return res;
    }

    public async post1() {
        console.log("post1");

        const res = await fetch(URL + '/post1', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                    userName: "sda",
                    password: "s"
                }
            )
        }).then( res =>{return res.ok });

        return res;
    }

    public async post2() {
        const res = await fetch(URL + '/post2', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                    userName: "sda",
                    password: "s"
                }
            )
        }).then( res =>{return res.ok });

        return res;
    }

    public async startGame(gameType: GameType, settings: any) {
        const res = await fetch(URL + '/start-game/' + gameType, {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(settings)
        });
    }



    public async move(moveDTO: any) {
        const res = await fetch(URL + '/move', {
            mode: 'cors',
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(moveDTO)
        }).then( res =>{return res.ok });

        return res;
    }

    public async leaveGroup() {
        let res = await fetch(URL + '/leave-group', {
            method: 'GET',
            credentials: 'include'
        });
    }

    public async sendGroupMessage(message: string) {

    }
}

export const api = new API();



