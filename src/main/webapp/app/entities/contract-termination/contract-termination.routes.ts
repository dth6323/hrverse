import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ContractTerminationResolve from './route/contract-termination-routing-resolve.service';

const contractTerminationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/contract-termination.component').then(m => m.ContractTerminationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/contract-termination-detail.component').then(m => m.ContractTerminationDetailComponent),
    resolve: {
      contractTermination: ContractTerminationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/contract-termination-update.component').then(m => m.ContractTerminationUpdateComponent),
    resolve: {
      contractTermination: ContractTerminationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/contract-termination-update.component').then(m => m.ContractTerminationUpdateComponent),
    resolve: {
      contractTermination: ContractTerminationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contractTerminationRoute;
