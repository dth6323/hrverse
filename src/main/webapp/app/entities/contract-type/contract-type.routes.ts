import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ContractTypeResolve from './route/contract-type-routing-resolve.service';

const contractTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/contract-type.component').then(m => m.ContractTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/contract-type-detail.component').then(m => m.ContractTypeDetailComponent),
    resolve: {
      contractType: ContractTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/contract-type-update.component').then(m => m.ContractTypeUpdateComponent),
    resolve: {
      contractType: ContractTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/contract-type-update.component').then(m => m.ContractTypeUpdateComponent),
    resolve: {
      contractType: ContractTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contractTypeRoute;
