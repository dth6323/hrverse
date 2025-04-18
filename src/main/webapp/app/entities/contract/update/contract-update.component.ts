import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContractType } from 'app/entities/contract-type/contract-type.model';
import { ContractTypeService } from 'app/entities/contract-type/service/contract-type.service';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
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

  contractTypesSharedCollection: IContractType[] = [];

  protected contractService = inject(ContractService);
  protected contractFormService = inject(ContractFormService);
  protected contractTypeService = inject(ContractTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

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

    this.contractTypesSharedCollection = this.contractTypeService.addContractTypeToCollectionIfMissing<IContractType>(
      this.contractTypesSharedCollection,
      contract.contractType,
    );
  }

  protected loadRelationshipsOptions(): void {
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
