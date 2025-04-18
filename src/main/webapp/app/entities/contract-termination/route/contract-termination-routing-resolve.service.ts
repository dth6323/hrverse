import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContractTermination } from '../contract-termination.model';
import { ContractTerminationService } from '../service/contract-termination.service';

const contractTerminationResolve = (route: ActivatedRouteSnapshot): Observable<null | IContractTermination> => {
  const id = route.params.id;
  if (id) {
    return inject(ContractTerminationService)
      .find(id)
      .pipe(
        mergeMap((contractTermination: HttpResponse<IContractTermination>) => {
          if (contractTermination.body) {
            return of(contractTermination.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default contractTerminationResolve;
