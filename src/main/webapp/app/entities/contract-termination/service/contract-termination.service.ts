import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContractTermination, NewContractTermination } from '../contract-termination.model';

export type PartialUpdateContractTermination = Partial<IContractTermination> & Pick<IContractTermination, 'id'>;

type RestOf<T extends IContractTermination | NewContractTermination> = Omit<T, 'terminationDate'> & {
  terminationDate?: string | null;
};

export type RestContractTermination = RestOf<IContractTermination>;

export type NewRestContractTermination = RestOf<NewContractTermination>;

export type PartialUpdateRestContractTermination = RestOf<PartialUpdateContractTermination>;

export type EntityResponseType = HttpResponse<IContractTermination>;
export type EntityArrayResponseType = HttpResponse<IContractTermination[]>;

@Injectable({ providedIn: 'root' })
export class ContractTerminationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contract-terminations');

  create(contractTermination: NewContractTermination): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractTermination);
    return this.http
      .post<RestContractTermination>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contractTermination: IContractTermination): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractTermination);
    return this.http
      .put<RestContractTermination>(`${this.resourceUrl}/${this.getContractTerminationIdentifier(contractTermination)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contractTermination: PartialUpdateContractTermination): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractTermination);
    return this.http
      .patch<RestContractTermination>(`${this.resourceUrl}/${this.getContractTerminationIdentifier(contractTermination)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContractTermination>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContractTermination[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContractTerminationIdentifier(contractTermination: Pick<IContractTermination, 'id'>): number {
    return contractTermination.id;
  }

  compareContractTermination(o1: Pick<IContractTermination, 'id'> | null, o2: Pick<IContractTermination, 'id'> | null): boolean {
    return o1 && o2 ? this.getContractTerminationIdentifier(o1) === this.getContractTerminationIdentifier(o2) : o1 === o2;
  }

  addContractTerminationToCollectionIfMissing<Type extends Pick<IContractTermination, 'id'>>(
    contractTerminationCollection: Type[],
    ...contractTerminationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contractTerminations: Type[] = contractTerminationsToCheck.filter(isPresent);
    if (contractTerminations.length > 0) {
      const contractTerminationCollectionIdentifiers = contractTerminationCollection.map(contractTerminationItem =>
        this.getContractTerminationIdentifier(contractTerminationItem),
      );
      const contractTerminationsToAdd = contractTerminations.filter(contractTerminationItem => {
        const contractTerminationIdentifier = this.getContractTerminationIdentifier(contractTerminationItem);
        if (contractTerminationCollectionIdentifiers.includes(contractTerminationIdentifier)) {
          return false;
        }
        contractTerminationCollectionIdentifiers.push(contractTerminationIdentifier);
        return true;
      });
      return [...contractTerminationsToAdd, ...contractTerminationCollection];
    }
    return contractTerminationCollection;
  }

  protected convertDateFromClient<T extends IContractTermination | NewContractTermination | PartialUpdateContractTermination>(
    contractTermination: T,
  ): RestOf<T> {
    return {
      ...contractTermination,
      terminationDate: contractTermination.terminationDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restContractTermination: RestContractTermination): IContractTermination {
    return {
      ...restContractTermination,
      terminationDate: restContractTermination.terminationDate ? dayjs(restContractTermination.terminationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContractTermination>): HttpResponse<IContractTermination> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContractTermination[]>): HttpResponse<IContractTermination[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
