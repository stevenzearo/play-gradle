export class SearchByNameResponse {
    public userInfos: UserInfo[];

    constructor(userInfos: UserInfo[]) {
        this.userInfos = userInfos;
    }
}

export class UserInfo {
    public id: number;
    public name: string;
    public age: number;

    constructor(id: number, name: string, age: number) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}