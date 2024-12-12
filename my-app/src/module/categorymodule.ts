export namespace CategoryInterface {

    export interface param {
        userId : string,
        categoryType ?: string
    }

    export interface retrivel {
        userId : string,
        categoryid ?: string,
        amount ?: number,
        name ?: string,
        description ?: string,
        type ?: string,
        date ?: string
    }

    export const name = "view-category";
    export const name_1 = "view-by-category";

}