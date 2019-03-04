import { IAsset } from 'app/shared/model/asset.model';

export interface ICategory {
    id?: number;
    category?: string;
    assets?: IAsset[];
    bankAccountId?: number;
    depreciationId?: number;
}

export class Category implements ICategory {
    constructor(
        public id?: number,
        public category?: string,
        public assets?: IAsset[],
        public bankAccountId?: number,
        public depreciationId?: number
    ) {}
}
