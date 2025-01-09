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


    export const view = "view-expense";
    export const deleteBy = "delete-expense";
    export const updateBy = "update-expense";

}