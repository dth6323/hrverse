import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IWage } from 'app/entities/wage/wage.model';
import { WageService } from 'app/entities/wage/service/wage.service';
import { ISalaryDistribute } from 'app/entities/salary-distribute/salary-distribute.model';
import { SalaryDistributeService } from 'app/entities/salary-distribute/service/salary-distribute.service';
import { PayrollService } from '../service/payroll.service';
import { IPayroll } from '../payroll.model';
import { PayrollFormGroup, PayrollFormService } from './payroll-form.service';

@Component({
  standalone: true,
  selector: 'jhi-payroll-update',
  templateUrl: './payroll-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PayrollUpdateComponent implements OnInit {
  isSaving = false;
  payroll: IPayroll | null = null;

  employeesSharedCollection: IEmployee[] = [];
  wagesSharedCollection: IWage[] = [];
  salaryDistributesSharedCollection: ISalaryDistribute[] = [];

  protected payrollService = inject(PayrollService);
  protected payrollFormService = inject(PayrollFormService);
  protected employeeService = inject(EmployeeService);
  protected wageService = inject(WageService);
  protected salaryDistributeService = inject(SalaryDistributeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PayrollFormGroup = this.payrollFormService.createPayrollFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareWage = (o1: IWage | null, o2: IWage | null): boolean => this.wageService.compareWage(o1, o2);

  compareSalaryDistribute = (o1: ISalaryDistribute | null, o2: ISalaryDistribute | null): boolean =>
    this.salaryDistributeService.compareSalaryDistribute(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payroll }) => {
      this.payroll = payroll;
      if (payroll) {
        this.updateForm(payroll);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payroll = this.payrollFormService.getPayroll(this.editForm);
    if (payroll.id !== null) {
      this.subscribeToSaveResponse(this.payrollService.update(payroll));
    } else {
      this.subscribeToSaveResponse(this.payrollService.create(payroll));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayroll>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(payroll: IPayroll): void {
    this.payroll = payroll;
    this.payrollFormService.resetForm(this.editForm, payroll);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      payroll.employee,
    );
    this.wagesSharedCollection = this.wageService.addWageToCollectionIfMissing<IWage>(this.wagesSharedCollection, payroll.wage);
    this.salaryDistributesSharedCollection = this.salaryDistributeService.addSalaryDistributeToCollectionIfMissing<ISalaryDistribute>(
      this.salaryDistributesSharedCollection,
      payroll.salaryDistribute,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.payroll?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.wageService
      .query()
      .pipe(map((res: HttpResponse<IWage[]>) => res.body ?? []))
      .pipe(map((wages: IWage[]) => this.wageService.addWageToCollectionIfMissing<IWage>(wages, this.payroll?.wage)))
      .subscribe((wages: IWage[]) => (this.wagesSharedCollection = wages));

    this.salaryDistributeService
      .query()
      .pipe(map((res: HttpResponse<ISalaryDistribute[]>) => res.body ?? []))
      .pipe(
        map((salaryDistributes: ISalaryDistribute[]) =>
          this.salaryDistributeService.addSalaryDistributeToCollectionIfMissing<ISalaryDistribute>(
            salaryDistributes,
            this.payroll?.salaryDistribute,
          ),
        ),
      )
      .subscribe((salaryDistributes: ISalaryDistribute[]) => (this.salaryDistributesSharedCollection = salaryDistributes));
  }
}
