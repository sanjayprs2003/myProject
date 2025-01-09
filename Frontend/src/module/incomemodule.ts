export namespace IncomeInterface {

    export interface param {
        userId : string,
        income ?: number
    }

    export interface retrivel {
        userId : number,
        income : number
    }

    export const view = "view-income";
    export const addBy = "add-income";

}