export interface IDepreciation {
    id?: number;
    typeOfDepreciation?: string;
    categoryId?: number;
}

export class Depreciation implements IDepreciation {
    constructor(public id?: number, public typeOfDepreciation?: string, public categoryId?: number) {}
}
