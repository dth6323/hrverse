import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISalaryDistribute } from '../salary-distribute.model';
import { SalaryDistributeService } from '../service/salary-distribute.service';
import { SalaryDistributeFormGroup, SalaryDistributeFormService } from './salary-distribute-form.service';

@Component({
  standalone: true,
  selector: 'jhi-salary-distribute-update',
  templateUrl: './salary-distribute-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SalaryDistributeUpdateComponent implements OnInit {
  isSaving = false;
  salaryDistribute: ISalaryDistribute | null = null;

  protected salaryDistributeService = inject(SalaryDistributeService);
  protected salaryDistributeFormService = inject(SalaryDistributeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SalaryDistributeFormGroup = this.salaryDistributeFormService.createSalaryDistributeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryDistribute }) => {
      this.salaryDistribute = salaryDistribute;
      if (salaryDistribute) {
        this.updateForm(salaryDistribute);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salaryDistribute = this.salaryDistributeFormService.getSalaryDistribute(this.editForm);
    if (salaryDistribute.id !== null) {
      this.subscribeToSaveResponse(this.salaryDistributeService.update(salaryDistribute));
    } else {
      this.subscribeToSaveResponse(this.salaryDistributeService.create(salaryDistribute));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalaryDistribute>>): void {
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

  protected updateForm(salaryDistribute: ISalaryDistribute): void {
    this.salaryDistribute = salaryDistribute;
    this.salaryDistributeFormService.resetForm(this.editForm, salaryDistribute);
  }
}
