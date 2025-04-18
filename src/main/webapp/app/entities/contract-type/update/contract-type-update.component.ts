import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContractType } from '../contract-type.model';
import { ContractTypeService } from '../service/contract-type.service';
import { ContractTypeFormGroup, ContractTypeFormService } from './contract-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-type-update',
  templateUrl: './contract-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractTypeUpdateComponent implements OnInit {
  isSaving = false;
  contractType: IContractType | null = null;

  protected contractTypeService = inject(ContractTypeService);
  protected contractTypeFormService = inject(ContractTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractTypeFormGroup = this.contractTypeFormService.createContractTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractType }) => {
      this.contractType = contractType;
      if (contractType) {
        this.updateForm(contractType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractType = this.contractTypeFormService.getContractType(this.editForm);
    if (contractType.id !== null) {
      this.subscribeToSaveResponse(this.contractTypeService.update(contractType));
    } else {
      this.subscribeToSaveResponse(this.contractTypeService.create(contractType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractType>>): void {
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

  protected updateForm(contractType: IContractType): void {
    this.contractType = contractType;
    this.contractTypeFormService.resetForm(this.editForm, contractType);
  }
}
