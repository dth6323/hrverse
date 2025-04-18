import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PayrollResolve from './route/payroll-routing-resolve.service';

const payrollRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/payroll.component').then(m => m.PayrollComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/payroll-detail.component').then(m => m.PayrollDetailComponent),
    resolve: {
      payroll: PayrollResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/payroll-update.component').then(m => m.PayrollUpdateComponent),
    resolve: {
      payroll: PayrollResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/payroll-update.component').then(m => m.PayrollUpdateComponent),
    resolve: {
      payroll: PayrollResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default payrollRoute;
