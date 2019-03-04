import { IAsset } from 'app/shared/model/asset.model';

export interface IServiceOutlet {
    id?: number;
    serviceOutlet?: string;
    serviceOutletCode?: string;
    assets?: IAsset[];
}

export class ServiceOutlet implements IServiceOutlet {
    constructor(public id?: number, public serviceOutlet?: string, public serviceOutletCode?: string, public assets?: IAsset[]) {}
}
