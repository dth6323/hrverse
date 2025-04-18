import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IContractTermination } from '../contract-termination.model';
import { ContractTerminationService } from '../service/contract-termination.service';
import { ContractTerminationFormGroup, ContractTerminationFormService } from './contract-termination-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-termination-update',
  templateUrl: './contract-termination-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractTerminationUpdateComponent implements OnInit {
  isSaving = false;
  contractTermination: IContractTermination | null = null;

  contractsSharedCollection: IContract[] = [];

  protected contractTerminationService = inject(ContractTerminationService);
  protected contractTerminationFormService = inject(ContractTerminationFormService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractTerminationFormGroup = this.contractTerminationFormService.createContractTerminationFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractTermination }) => {
      this.contractTermination = contractTermination;
      if (contractTermination) {
        this.updateForm(contractTermination);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractTermination = this.contractTerminationFormService.getContractTermination(this.editForm);
    if (contractTermination.id !== null) {
      this.subscribeToSaveResponse(this.contractTerminationService.update(contractTermination));
    } else {
      this.subscribeToSaveResponse(this.contractTerminationService.create(contractTermination));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractTermination>>): void {
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

  protected updateForm(contractTermination: IContractTermination): void {
    this.contractTermination = contractTermination;
    this.contractTerminationFormService.resetForm(this.editForm, contractTermination);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      contractTermination.contract,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.contractTermination?.contract),
        ),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));
  }
}
