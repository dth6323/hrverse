import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPayroll } from '../payroll.model';
import { PayrollService } from '../service/payroll.service';

const payrollResolve = (route: ActivatedRouteSnapshot): Observable<null | IPayroll> => {
  const id = route.params.id;
  if (id) {
    return inject(PayrollService)
      .find(id)
      .pipe(
        mergeMap((payroll: HttpResponse<IPayroll>) => {
          if (payroll.body) {
            return of(payroll.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default payrollResolve;
