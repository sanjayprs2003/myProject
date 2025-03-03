export namespace LoginInterface {

    export interface param {
        username: string,
        password: string,
    }

    export interface retrivel {
        userId: string,
        accessToken: string,
        refreshToken: string,
        success: string
    }

    export const name = "login";

}