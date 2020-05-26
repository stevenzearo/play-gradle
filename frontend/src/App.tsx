import React from 'react';

import './App.css';
import {UserWebService} from "./api/UserWebService";
import {GetUserResponse} from "./api/user/GetUserResponse";

function App() {
    var data: GetUserResponse | null = UserWebService.get(1);
    return (
        <div className="App">
            <header className="App-header">
                <div className="login">
                    <ul>
                        <li>id:{data?.id}</li>
                        <li>name:{data?.name}</li>
                        <li>age:{data?.age}</li>
                    </ul>
                </div>
            </header>
        </div>
    );
}

export default App;
