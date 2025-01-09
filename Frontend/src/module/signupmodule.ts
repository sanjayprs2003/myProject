export namespace SignupInterface {

    export interface param {
        username : string,
        password : string
    }

    export interface retrivel {
        success : string,
        message : string
    }

    export const name = "add-user";

}