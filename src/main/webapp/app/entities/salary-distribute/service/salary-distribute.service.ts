import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalaryDistribute, NewSalaryDistribute } from '../salary-distribute.model';

export type PartialUpdateSalaryDistribute = Partial<ISalaryDistribute> & Pick<ISalaryDistribute, 'id'>;

type RestOf<T extends ISalaryDistribute | NewSalaryDistribute> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestSalaryDistribute = RestOf<ISalaryDistribute>;

export type NewRestSalaryDistribute = RestOf<NewSalaryDistribute>;

export type PartialUpdateRestSalaryDistribute = RestOf<PartialUpdateSalaryDistribute>;

export type EntityResponseType = HttpResponse<ISalaryDistribute>;
export type EntityArrayResponseType = HttpResponse<ISalaryDistribute[]>;

@Injectable({ providedIn: 'root' })
export class SalaryDistributeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/salary-distributes');

  create(salaryDistribute: NewSalaryDistribute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryDistribute);
    return this.http
      .post<RestSalaryDistribute>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(salaryDistribute: ISalaryDistribute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryDistribute);
    return this.http
      .put<RestSalaryDistribute>(`${this.resourceUrl}/${this.getSalaryDistributeIdentifier(salaryDistribute)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(salaryDistribute: PartialUpdateSalaryDistribute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryDistribute);
    return this.http
      .patch<RestSalaryDistribute>(`${this.resourceUrl}/${this.getSalaryDistributeIdentifier(salaryDistribute)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSalaryDistribute>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSalaryDistribute[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSalaryDistributeIdentifier(salaryDistribute: Pick<ISalaryDistribute, 'id'>): number {
    return salaryDistribute.id;
  }

  compareSalaryDistribute(o1: Pick<ISalaryDistribute, 'id'> | null, o2: Pick<ISalaryDistribute, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalaryDistributeIdentifier(o1) === this.getSalaryDistributeIdentifier(o2) : o1 === o2;
  }

  addSalaryDistributeToCollectionIfMissing<Type extends Pick<ISalaryDistribute, 'id'>>(
    salaryDistributeCollection: Type[],
    ...salaryDistributesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salaryDistributes: Type[] = salaryDistributesToCheck.filter(isPresent);
    if (salaryDistributes.length > 0) {
      const salaryDistributeCollectionIdentifiers = salaryDistributeCollection.map(salaryDistributeItem =>
        this.getSalaryDistributeIdentifier(salaryDistributeItem),
      );
      const salaryDistributesToAdd = salaryDistributes.filter(salaryDistributeItem => {
        const salaryDistributeIdentifier = this.getSalaryDistributeIdentifier(salaryDistributeItem);
        if (salaryDistributeCollectionIdentifiers.includes(salaryDistributeIdentifier)) {
          return false;
        }
        salaryDistributeCollectionIdentifiers.push(salaryDistributeIdentifier);
        return true;
      });
      return [...salaryDistributesToAdd, ...salaryDistributeCollection];
    }
    return salaryDistributeCollection;
  }

  protected convertDateFromClient<T extends ISalaryDistribute | NewSalaryDistribute | PartialUpdateSalaryDistribute>(
    salaryDistribute: T,
  ): RestOf<T> {
    return {
      ...salaryDistribute,
      startDate: salaryDistribute.startDate?.format(DATE_FORMAT) ?? null,
      endDate: salaryDistribute.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSalaryDistribute: RestSalaryDistribute): ISalaryDistribute {
    return {
      ...restSalaryDistribute,
      startDate: restSalaryDistribute.startDate ? dayjs(restSalaryDistribute.startDate) : undefined,
      endDate: restSalaryDistribute.endDate ? dayjs(restSalaryDistribute.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSalaryDistribute>): HttpResponse<ISalaryDistribute> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSalaryDistribute[]>): HttpResponse<ISalaryDistribute[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
