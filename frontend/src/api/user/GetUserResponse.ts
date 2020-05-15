export class GetUserResponse {
    public id: number;
    public name: string;
    public age: number;
    public email: string;

    constructor(id: number, name: string, age: number, email: string) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }
}