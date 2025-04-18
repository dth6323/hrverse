import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRewardPunishment } from '../reward-punishment.model';
import { RewardPunishmentService } from '../service/reward-punishment.service';
import { RewardPunishmentFormGroup, RewardPunishmentFormService } from './reward-punishment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reward-punishment-update',
  templateUrl: './reward-punishment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RewardPunishmentUpdateComponent implements OnInit {
  isSaving = false;
  rewardPunishment: IRewardPunishment | null = null;

  employeesSharedCollection: IEmployee[] = [];

  protected rewardPunishmentService = inject(RewardPunishmentService);
  protected rewardPunishmentFormService = inject(RewardPunishmentFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RewardPunishmentFormGroup = this.rewardPunishmentFormService.createRewardPunishmentFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rewardPunishment }) => {
      this.rewardPunishment = rewardPunishment;
      if (rewardPunishment) {
        this.updateForm(rewardPunishment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rewardPunishment = this.rewardPunishmentFormService.getRewardPunishment(this.editForm);
    if (rewardPunishment.id !== null) {
      this.subscribeToSaveResponse(this.rewardPunishmentService.update(rewardPunishment));
    } else {
      this.subscribeToSaveResponse(this.rewardPunishmentService.create(rewardPunishment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRewardPunishment>>): void {
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

  protected updateForm(rewardPunishment: IRewardPunishment): void {
    this.rewardPunishment = rewardPunishment;
    this.rewardPunishmentFormService.resetForm(this.editForm, rewardPunishment);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      rewardPunishment.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.rewardPunishment?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
