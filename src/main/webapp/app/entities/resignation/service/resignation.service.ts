import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResignation, NewResignation } from '../resignation.model';

export type PartialUpdateResignation = Partial<IResignation> & Pick<IResignation, 'id'>;

type RestOf<T extends IResignation | NewResignation> = Omit<T, 'submissionDate' | 'effectiveDate'> & {
  submissionDate?: string | null;
  effectiveDate?: string | null;
};

export type RestResignation = RestOf<IResignation>;

export type NewRestResignation = RestOf<NewResignation>;

export type PartialUpdateRestResignation = RestOf<PartialUpdateResignation>;

export type EntityResponseType = HttpResponse<IResignation>;
export type EntityArrayResponseType = HttpResponse<IResignation[]>;

@Injectable({ providedIn: 'root' })
export class ResignationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resignations');

  create(resignation: NewResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resignation);
    return this.http
      .post<RestResignation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(resignation: IResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resignation);
    return this.http
      .put<RestResignation>(`${this.resourceUrl}/${this.getResignationIdentifier(resignation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(resignation: PartialUpdateResignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resignation);
    return this.http
      .patch<RestResignation>(`${this.resourceUrl}/${this.getResignationIdentifier(resignation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestResignation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestResignation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResignationIdentifier(resignation: Pick<IResignation, 'id'>): number {
    return resignation.id;
  }

  compareResignation(o1: Pick<IResignation, 'id'> | null, o2: Pick<IResignation, 'id'> | null): boolean {
    return o1 && o2 ? this.getResignationIdentifier(o1) === this.getResignationIdentifier(o2) : o1 === o2;
  }

  addResignationToCollectionIfMissing<Type extends Pick<IResignation, 'id'>>(
    resignationCollection: Type[],
    ...resignationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const resignations: Type[] = resignationsToCheck.filter(isPresent);
    if (resignations.length > 0) {
      const resignationCollectionIdentifiers = resignationCollection.map(resignationItem => this.getResignationIdentifier(resignationItem));
      const resignationsToAdd = resignations.filter(resignationItem => {
        const resignationIdentifier = this.getResignationIdentifier(resignationItem);
        if (resignationCollectionIdentifiers.includes(resignationIdentifier)) {
          return false;
        }
        resignationCollectionIdentifiers.push(resignationIdentifier);
        return true;
      });
      return [...resignationsToAdd, ...resignationCollection];
    }
    return resignationCollection;
  }

  protected convertDateFromClient<T extends IResignation | NewResignation | PartialUpdateResignation>(resignation: T): RestOf<T> {
    return {
      ...resignation,
      submissionDate: resignation.submissionDate?.format(DATE_FORMAT) ?? null,
      effectiveDate: resignation.effectiveDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restResignation: RestResignation): IResignation {
    return {
      ...restResignation,
      submissionDate: restResignation.submissionDate ? dayjs(restResignation.submissionDate) : undefined,
      effectiveDate: restResignation.effectiveDate ? dayjs(restResignation.effectiveDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestResignation>): HttpResponse<IResignation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestResignation[]>): HttpResponse<IResignation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
