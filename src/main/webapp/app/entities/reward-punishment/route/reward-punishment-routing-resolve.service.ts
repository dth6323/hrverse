import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRewardPunishment } from '../reward-punishment.model';
import { RewardPunishmentService } from '../service/reward-punishment.service';

const rewardPunishmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IRewardPunishment> => {
  const id = route.params.id;
  if (id) {
    return inject(RewardPunishmentService)
      .find(id)
      .pipe(
        mergeMap((rewardPunishment: HttpResponse<IRewardPunishment>) => {
          if (rewardPunishment.body) {
            return of(rewardPunishment.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default rewardPunishmentResolve;
