import * as React from 'react';
import ReactDOM from 'react-dom';
import './css/index.css';
import App from './App';
import {BrowserRouter, MemoryRouter} from "react-router-dom";
import * as serviceWorker from './serviceWorker';
import {  Redirect, Route, Switch} from "react-router-dom";

ReactDOM.render(
    <BrowserRouter>
        <Route component={App}/>
    </BrowserRouter>
    ,
    document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
