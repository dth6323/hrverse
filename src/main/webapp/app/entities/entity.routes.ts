import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'hrVerseApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'attendance',
    data: { pageTitle: 'hrVerseApp.attendance.home.title' },
    loadChildren: () => import('./attendance/attendance.routes'),
  },
  {
    path: 'contract',
    data: { pageTitle: 'hrVerseApp.contract.home.title' },
    loadChildren: () => import('./contract/contract.routes'),
  },
  {
    path: 'contract-termination',
    data: { pageTitle: 'hrVerseApp.contractTermination.home.title' },
    loadChildren: () => import('./contract-termination/contract-termination.routes'),
  },
  {
    path: 'contract-type',
    data: { pageTitle: 'hrVerseApp.contractType.home.title' },
    loadChildren: () => import('./contract-type/contract-type.routes'),
  },
  {
    path: 'department',
    data: { pageTitle: 'hrVerseApp.department.home.title' },
    loadChildren: () => import('./department/department.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'hrVerseApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'payroll',
    data: { pageTitle: 'hrVerseApp.payroll.home.title' },
    loadChildren: () => import('./payroll/payroll.routes'),
  },
  {
    path: 'resignation',
    data: { pageTitle: 'hrVerseApp.resignation.home.title' },
    loadChildren: () => import('./resignation/resignation.routes'),
  },
  {
    path: 'reward-punishment',
    data: { pageTitle: 'hrVerseApp.rewardPunishment.home.title' },
    loadChildren: () => import('./reward-punishment/reward-punishment.routes'),
  },
  {
    path: 'salary-distribute',
    data: { pageTitle: 'hrVerseApp.salaryDistribute.home.title' },
    loadChildren: () => import('./salary-distribute/salary-distribute.routes'),
  },
  {
    path: 'wage',
    data: { pageTitle: 'hrVerseApp.wage.home.title' },
    loadChildren: () => import('./wage/wage.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
