import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RewardPunishmentResolve from './route/reward-punishment-routing-resolve.service';

const rewardPunishmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reward-punishment.component').then(m => m.RewardPunishmentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reward-punishment-detail.component').then(m => m.RewardPunishmentDetailComponent),
    resolve: {
      rewardPunishment: RewardPunishmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reward-punishment-update.component').then(m => m.RewardPunishmentUpdateComponent),
    resolve: {
      rewardPunishment: RewardPunishmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reward-punishment-update.component').then(m => m.RewardPunishmentUpdateComponent),
    resolve: {
      rewardPunishment: RewardPunishmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rewardPunishmentRoute;
