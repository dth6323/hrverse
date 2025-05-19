import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Observable, Subject, switchMap } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { EmployeeService } from '../entities/employee/service/employee.service';
import { IEmployee } from '../entities/employee/employee.model';
import { Infor } from '../entities/employee/infor.model';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);
  employeeData: Infor[] = [];
  private readonly destroy$ = new Subject<void>();
  private accountService = inject(AccountService);
  private router = inject(Router);
  private employeeService = inject(EmployeeService);
  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));
    if (this.account()) {
      this.employeeService.getInformation().subscribe({
        next: data => {
          //eslint-disable-next-line
          console.log(data);
          this.employeeData = data;
        },
      });
    }
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
