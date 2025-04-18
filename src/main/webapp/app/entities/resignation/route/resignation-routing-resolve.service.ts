import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResignation } from '../resignation.model';
import { ResignationService } from '../service/resignation.service';

const resignationResolve = (route: ActivatedRouteSnapshot): Observable<null | IResignation> => {
  const id = route.params.id;
  if (id) {
    return inject(ResignationService)
      .find(id)
      .pipe(
        mergeMap((resignation: HttpResponse<IResignation>) => {
          if (resignation.body) {
            return of(resignation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default resignationResolve;
