import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IWage } from '../wage.model';
import { WageService } from '../service/wage.service';
import { WageFormGroup, WageFormService } from './wage-form.service';

@Component({
  standalone: true,
  selector: 'jhi-wage-update',
  templateUrl: './wage-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WageUpdateComponent implements OnInit {
  isSaving = false;
  wage: IWage | null = null;

  protected wageService = inject(WageService);
  protected wageFormService = inject(WageFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WageFormGroup = this.wageFormService.createWageFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wage }) => {
      this.wage = wage;
      if (wage) {
        this.updateForm(wage);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wage = this.wageFormService.getWage(this.editForm);
    if (wage.id !== null) {
      this.subscribeToSaveResponse(this.wageService.update(wage));
    } else {
      this.subscribeToSaveResponse(this.wageService.create(wage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWage>>): void {
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

  protected updateForm(wage: IWage): void {
    this.wage = wage;
    this.wageFormService.resetForm(this.editForm, wage);
  }
}
