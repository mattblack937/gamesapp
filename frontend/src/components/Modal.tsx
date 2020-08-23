import React, {CSSProperties, ReactNode, useContext, useEffect} from 'react';
import {api} from "../util/API";

import {AppContext, LOG_ON} from "../App";
import '../css/header.css';

import { ReactComponent as LogoutLogo } from '../svg/logout.svg';
import { ReactComponent as ReconnectLogo } from '../svg/reconnect.svg';

import {Key} from "ts-keycode-enum";

type ModalProps = {
    children: ReactNode,
    isOpen: boolean,
    closeOnBackGroundClick?: boolean,
    close?: ()=> any,
    onOpen?: ()=> {}
}

export function Modal ({children, isOpen, closeOnBackGroundClick, onOpen, close}: ModalProps) {

    useEffect(() => {
        onOpen && onOpen();
    }, []);

    if (!isOpen){
        return null;
    }

    return (
        <div className={"modal"} >
            <div className={"modal-background"} onClick={()=> {closeOnBackGroundClick && close && close()}}>
            </div>
            <div className={"modal-content"}>
                {children}
            </div>
        </div>
    );
}

















