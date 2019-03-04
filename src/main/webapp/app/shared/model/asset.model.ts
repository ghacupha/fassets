import { Moment } from 'moment';

export interface IAsset {
    id?: number;
    description?: string;
    purchaseDate?: Moment;
    assetTag?: string;
    purchaseCost?: number;
    categoryId?: number;
    serviceOutletId?: number;
}

export class Asset implements IAsset {
    constructor(
        public id?: number,
        public description?: string,
        public purchaseDate?: Moment,
        public assetTag?: string,
        public purchaseCost?: number,
        public categoryId?: number,
        public serviceOutletId?: number
    ) {}
}
