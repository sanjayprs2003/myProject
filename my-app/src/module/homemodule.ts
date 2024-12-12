export namespace HomeInterface {

    export interface param {
        userId : string
        categoryid ?: string,
        amount ?: number,
        name ?: string,
        description ?: string,
        type ?: string,
        date ?: string
    }

    export interface retrivel {
        userId : string,
        categoryid : string,
        amount : number,
        name : string,
        description : string,
        type : string,
        date : string
    }


    export const name = "view-expense";
    export const name_1 = "delete-expense";
    export const name_2 = "update-expense";

}