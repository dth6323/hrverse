import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWage } from '../wage.model';
import { WageService } from '../service/wage.service';

const wageResolve = (route: ActivatedRouteSnapshot): Observable<null | IWage> => {
  const id = route.params.id;
  if (id) {
    return inject(WageService)
      .find(id)
      .pipe(
        mergeMap((wage: HttpResponse<IWage>) => {
          if (wage.body) {
            return of(wage.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default wageResolve;
