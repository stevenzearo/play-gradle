import $ from 'jquery';
import {CreateUserRequest} from "./user/CreateUserRequest";
import {GetUserResponse} from "./user/GetUserResponse";
import {SearchByNameResponse, UserInfo} from "./user/SearchByNameResponse";

export class UserWebService {
    static get(id: number): GetUserResponse | null {
        var result: GetUserResponse | null = null;
        $.ajax({
            async: false,
            type: 'GET',
            url: 'http://localhost:9000/user/' + id,
            success: (data, status, xhr) => {
                console.log("get user success!!!!!");
                console.log(data);
                result = new GetUserResponse(data.id, data.name, data.age);
                return result;
            },
            dataType: 'json'
        });

        return result;
    }

    static create(user: CreateUserRequest) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost/',
            data: user,
            dataType: 'application/json'
        })
    }

    static searchByName(name: string) : SearchByNameResponse | null {
        var result: SearchByNameResponse | null = null;
        $.ajax({
            type: 'PUT',
            url: 'http://localhost/9000/user',
            data: {name: name},
            dataType: 'application/json',
            success: (data, status, xhr) => {
                console.log("get user success!!!!!");
                console.log(data);
                let userInfos = [];
                for (let i = 0; i < data.length; i++) {
                    userInfos[i] = new UserInfo(data[i].id, data[i].age, data[i].name);
                }
                return new SearchByNameResponse(userInfos);
            }
        });
        return result;
    }
}