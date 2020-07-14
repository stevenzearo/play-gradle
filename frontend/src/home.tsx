import React from 'react';
import ShipWar from './module/game/shipWar';

export interface HomeProp {

}

export interface HomeState {

}

export class Home extends React.Component<HomeProp, HomeState> {

    state: HomeProp = {};

    render() {
        return (
            <div className={"content"}>
                <h1>Hello, world!</h1>
                <ShipWar/>
            </div>
        );
    }
}

export default Home;
