import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IResignation } from '../resignation.model';
import { ResignationService } from '../service/resignation.service';
import { ResignationFormGroup, ResignationFormService } from './resignation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-resignation-update',
  templateUrl: './resignation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResignationUpdateComponent implements OnInit {
  isSaving = false;
  resignation: IResignation | null = null;

  employeesSharedCollection: IEmployee[] = [];

  protected resignationService = inject(ResignationService);
  protected resignationFormService = inject(ResignationFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResignationFormGroup = this.resignationFormService.createResignationFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resignation }) => {
      this.resignation = resignation;
      if (resignation) {
        this.updateForm(resignation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resignation = this.resignationFormService.getResignation(this.editForm);
    if (resignation.id !== null) {
      this.subscribeToSaveResponse(this.resignationService.update(resignation));
    } else {
      this.subscribeToSaveResponse(this.resignationService.create(resignation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResignation>>): void {
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

  protected updateForm(resignation: IResignation): void {
    this.resignation = resignation;
    this.resignationFormService.resetForm(this.editForm, resignation);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      resignation.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.resignation?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
