import React from 'react';
import {IBlogProps, IBlogState} from "./common/IBlogProps";
import {UserInfo} from "./api/user/SearchByNameResponse";

class Home extends React.Component<IBlogProps, IBlogState> {
    private user: UserInfo | null;


    constructor(props: Readonly<IBlogProps>) {
        super(props);
        this.user = props.user;
    }

    state: IBlogState = {
        user: this.user
    };

    render() {
        return (
            <div>
                <h1>Hello, world!</h1>
                <ul>
                    user info:
                    <li>id: {this.user?.id}</li>
                    <li>name: {this.user?.name}</li>
                    <li>age: {this.user?.age}</li>
                </ul>
            </div>
        );
    }
}

export default Home;