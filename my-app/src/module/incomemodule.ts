export namespace IncomeInterface {

    export interface param {
        userId : string,
        income ?: number
    }

    export interface retrivel {
        userId : number,
        income : number
    }

    export const name = "view-income";
    export const name_1 = "add-income";

}