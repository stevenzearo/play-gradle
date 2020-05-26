import {UserInfo} from "../api/user/SearchByNameResponse";

export interface IBlogProps {
    user: UserInfo | null;
}

export interface IBlogState {
    user: UserInfo | null;

}