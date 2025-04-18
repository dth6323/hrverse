import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AttendanceResolve from './route/attendance-routing-resolve.service';

const attendanceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/attendance.component').then(m => m.AttendanceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/attendance-detail.component').then(m => m.AttendanceDetailComponent),
    resolve: {
      attendance: AttendanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/attendance-update.component').then(m => m.AttendanceUpdateComponent),
    resolve: {
      attendance: AttendanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/attendance-update.component').then(m => m.AttendanceUpdateComponent),
    resolve: {
      attendance: AttendanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default attendanceRoute;
