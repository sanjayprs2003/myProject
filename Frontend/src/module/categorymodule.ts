export namespace CategoryInterface {

    export interface param {
        userId: string,
        categoryType?: string
    }

    export interface retrivel {
        userId: string,
        categoryid?: string,
        amount?: number,
        name?: string,
        description?: string,
        type?: string,
        date?: string
    }

    export const view = "view-category";
    export const viewBy = "view-by-category";

}