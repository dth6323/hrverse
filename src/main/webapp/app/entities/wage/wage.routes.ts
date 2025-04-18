import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WageResolve from './route/wage-routing-resolve.service';

const wageRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/wage.component').then(m => m.WageComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/wage-detail.component').then(m => m.WageDetailComponent),
    resolve: {
      wage: WageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/wage-update.component').then(m => m.WageUpdateComponent),
    resolve: {
      wage: WageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/wage-update.component').then(m => m.WageUpdateComponent),
    resolve: {
      wage: WageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default wageRoute;
