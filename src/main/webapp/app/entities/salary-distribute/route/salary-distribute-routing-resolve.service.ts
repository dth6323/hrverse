import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalaryDistribute } from '../salary-distribute.model';
import { SalaryDistributeService } from '../service/salary-distribute.service';

const salaryDistributeResolve = (route: ActivatedRouteSnapshot): Observable<null | ISalaryDistribute> => {
  const id = route.params.id;
  if (id) {
    return inject(SalaryDistributeService)
      .find(id)
      .pipe(
        mergeMap((salaryDistribute: HttpResponse<ISalaryDistribute>) => {
          if (salaryDistribute.body) {
            return of(salaryDistribute.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default salaryDistributeResolve;
