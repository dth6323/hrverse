import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ResignationResolve from './route/resignation-routing-resolve.service';

const resignationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/resignation.component').then(m => m.ResignationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/resignation-detail.component').then(m => m.ResignationDetailComponent),
    resolve: {
      resignation: ResignationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/resignation-update.component').then(m => m.ResignationUpdateComponent),
    resolve: {
      resignation: ResignationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/resignation-update.component').then(m => m.ResignationUpdateComponent),
    resolve: {
      resignation: ResignationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default resignationRoute;
