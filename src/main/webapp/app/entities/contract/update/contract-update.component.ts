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
import { IContractType } from 'app/entities/contract-type/contract-type.model';
import { ContractTypeService } from 'app/entities/contract-type/service/contract-type.service';
import { Status } from 'app/entities/enumerations/status.model';
import { ContractService } from '../service/contract.service';
import { IContract } from '../contract.model';
import { ContractFormGroup, ContractFormService } from './contract-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;
  contract: IContract | null = null;
  statusValues = Object.keys(Status);

  employeesSharedCollection: IEmployee[] = [];
  wagesSharedCollection: IWage[] = [];
  contractTypesSharedCollection: IContractType[] = [];

  protected contractService = inject(ContractService);
  protected contractFormService = inject(ContractFormService);
  protected employeeService = inject(EmployeeService);
  protected wageService = inject(WageService);
  protected contractTypeService = inject(ContractTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareWage = (o1: IWage | null, o2: IWage | null): boolean => this.wageService.compareWage(o1, o2);

  compareContractType = (o1: IContractType | null, o2: IContractType | null): boolean =>
    this.contractTypeService.compareContractType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.contract = contract;
      if (contract) {
        this.updateForm(contract);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.contractFormService.getContract(this.editForm);
    if (contract.id !== null) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
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

  protected updateForm(contract: IContract): void {
    this.contract = contract;
    this.contractFormService.resetForm(this.editForm, contract);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      contract.employee,
    );
    this.wagesSharedCollection = this.wageService.addWageToCollectionIfMissing<IWage>(this.wagesSharedCollection, contract.wage);
    this.contractTypesSharedCollection = this.contractTypeService.addContractTypeToCollectionIfMissing<IContractType>(
      this.contractTypesSharedCollection,
      contract.contractType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.contract?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.wageService
      .query()
      .pipe(map((res: HttpResponse<IWage[]>) => res.body ?? []))
      .pipe(map((wages: IWage[]) => this.wageService.addWageToCollectionIfMissing<IWage>(wages, this.contract?.wage)))
      .subscribe((wages: IWage[]) => (this.wagesSharedCollection = wages));

    this.contractTypeService
      .query()
      .pipe(map((res: HttpResponse<IContractType[]>) => res.body ?? []))
      .pipe(
        map((contractTypes: IContractType[]) =>
          this.contractTypeService.addContractTypeToCollectionIfMissing<IContractType>(contractTypes, this.contract?.contractType),
        ),
      )
      .subscribe((contractTypes: IContractType[]) => (this.contractTypesSharedCollection = contractTypes));
  }
}
