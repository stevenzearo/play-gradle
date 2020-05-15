import $ from 'jquery';
import {CreateUserRequest} from "./user/CreateUserRequest";
import {GetUserResponse} from "./user/GetUserResponse";

export class UserWebService {
    get(id: number): GetUserResponse | null {
        var result: GetUserResponse | null = null;
        $.ajax({
            async: false,
            type: 'GET',
            url: 'http://localhost:8410/user/' + id,
            success: (data,status,xhr) => {
                console.log("get user success!!!!!");
                console.log(data);
                result = new GetUserResponse(data.id, data.name, data.age, data.email);
                return result;
            },
            dataType: 'json'
        });

        return result;
    }

    create(user: CreateUserRequest) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost/',
            data: user,
            dataType: 'application/json'
        })
    }

}