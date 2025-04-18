import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SalaryDistributeResolve from './route/salary-distribute-routing-resolve.service';

const salaryDistributeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/salary-distribute.component').then(m => m.SalaryDistributeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/salary-distribute-detail.component').then(m => m.SalaryDistributeDetailComponent),
    resolve: {
      salaryDistribute: SalaryDistributeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/salary-distribute-update.component').then(m => m.SalaryDistributeUpdateComponent),
    resolve: {
      salaryDistribute: SalaryDistributeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/salary-distribute-update.component').then(m => m.SalaryDistributeUpdateComponent),
    resolve: {
      salaryDistribute: SalaryDistributeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default salaryDistributeRoute;
