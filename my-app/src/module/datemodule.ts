export namespace DateInterface {

    export interface param {
        userId : string,
        startDate ?: string
        lastDate ?: string,
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

    export const name = "view-by-date";

}