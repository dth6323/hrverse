import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';

const payrollRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/filemana.component').then(m => m.FileComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default payrollRoute;
