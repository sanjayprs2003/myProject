export namespace LoginInterface {

    export interface param {
        username: string,
        password: string,
    }

    export interface retrivel {
        userId: string,
        token: string,
        message?: string,
        success?: string
    }

    export const name = "login";

}