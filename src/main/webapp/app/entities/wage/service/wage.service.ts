import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWage, NewWage } from '../wage.model';

export type PartialUpdateWage = Partial<IWage> & Pick<IWage, 'id'>;

export type EntityResponseType = HttpResponse<IWage>;
export type EntityArrayResponseType = HttpResponse<IWage[]>;

@Injectable({ providedIn: 'root' })
export class WageService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wages');

  create(wage: NewWage): Observable<EntityResponseType> {
    return this.http.post<IWage>(this.resourceUrl, wage, { observe: 'response' });
  }

  update(wage: IWage): Observable<EntityResponseType> {
    return this.http.put<IWage>(`${this.resourceUrl}/${this.getWageIdentifier(wage)}`, wage, { observe: 'response' });
  }

  partialUpdate(wage: PartialUpdateWage): Observable<EntityResponseType> {
    return this.http.patch<IWage>(`${this.resourceUrl}/${this.getWageIdentifier(wage)}`, wage, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWageIdentifier(wage: Pick<IWage, 'id'>): number {
    return wage.id;
  }

  compareWage(o1: Pick<IWage, 'id'> | null, o2: Pick<IWage, 'id'> | null): boolean {
    return o1 && o2 ? this.getWageIdentifier(o1) === this.getWageIdentifier(o2) : o1 === o2;
  }

  addWageToCollectionIfMissing<Type extends Pick<IWage, 'id'>>(
    wageCollection: Type[],
    ...wagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const wages: Type[] = wagesToCheck.filter(isPresent);
    if (wages.length > 0) {
      const wageCollectionIdentifiers = wageCollection.map(wageItem => this.getWageIdentifier(wageItem));
      const wagesToAdd = wages.filter(wageItem => {
        const wageIdentifier = this.getWageIdentifier(wageItem);
        if (wageCollectionIdentifiers.includes(wageIdentifier)) {
          return false;
        }
        wageCollectionIdentifiers.push(wageIdentifier);
        return true;
      });
      return [...wagesToAdd, ...wageCollection];
    }
    return wageCollection;
  }
}
