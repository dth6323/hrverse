import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPayroll, NewPayroll } from '../payroll.model';

export type PartialUpdatePayroll = Partial<IPayroll> & Pick<IPayroll, 'id'>;

export type EntityResponseType = HttpResponse<IPayroll>;
export type EntityArrayResponseType = HttpResponse<IPayroll[]>;

@Injectable({ providedIn: 'root' })
export class PayrollService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payrolls');

  create(payroll: NewPayroll): Observable<EntityResponseType> {
    return this.http.post<IPayroll>(this.resourceUrl, payroll, { observe: 'response' });
  }

  update(payroll: IPayroll): Observable<EntityResponseType> {
    return this.http.put<IPayroll>(`${this.resourceUrl}/${this.getPayrollIdentifier(payroll)}`, payroll, { observe: 'response' });
  }

  partialUpdate(payroll: PartialUpdatePayroll): Observable<EntityResponseType> {
    return this.http.patch<IPayroll>(`${this.resourceUrl}/${this.getPayrollIdentifier(payroll)}`, payroll, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPayroll>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPayroll[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPayrollIdentifier(payroll: Pick<IPayroll, 'id'>): number {
    return payroll.id;
  }

  comparePayroll(o1: Pick<IPayroll, 'id'> | null, o2: Pick<IPayroll, 'id'> | null): boolean {
    return o1 && o2 ? this.getPayrollIdentifier(o1) === this.getPayrollIdentifier(o2) : o1 === o2;
  }

  addPayrollToCollectionIfMissing<Type extends Pick<IPayroll, 'id'>>(
    payrollCollection: Type[],
    ...payrollsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const payrolls: Type[] = payrollsToCheck.filter(isPresent);
    if (payrolls.length > 0) {
      const payrollCollectionIdentifiers = payrollCollection.map(payrollItem => this.getPayrollIdentifier(payrollItem));
      const payrollsToAdd = payrolls.filter(payrollItem => {
        const payrollIdentifier = this.getPayrollIdentifier(payrollItem);
        if (payrollCollectionIdentifiers.includes(payrollIdentifier)) {
          return false;
        }
        payrollCollectionIdentifiers.push(payrollIdentifier);
        return true;
      });
      return [...payrollsToAdd, ...payrollCollection];
    }
    return payrollCollection;
  }
}
