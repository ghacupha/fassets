import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDepreciation } from 'app/shared/model/depreciation.model';

type EntityResponseType = HttpResponse<IDepreciation>;
type EntityArrayResponseType = HttpResponse<IDepreciation[]>;

@Injectable({ providedIn: 'root' })
export class DepreciationService {
    public resourceUrl = SERVER_API_URL + 'api/depreciations';

    constructor(protected http: HttpClient) {}

    create(depreciation: IDepreciation): Observable<EntityResponseType> {
        return this.http.post<IDepreciation>(this.resourceUrl, depreciation, { observe: 'response' });
    }

    update(depreciation: IDepreciation): Observable<EntityResponseType> {
        return this.http.put<IDepreciation>(this.resourceUrl, depreciation, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDepreciation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDepreciation[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
