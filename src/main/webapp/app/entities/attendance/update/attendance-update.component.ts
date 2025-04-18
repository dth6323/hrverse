import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IAttendance } from '../attendance.model';
import { AttendanceService } from '../service/attendance.service';
import { AttendanceFormGroup, AttendanceFormService } from './attendance-form.service';

@Component({
  standalone: true,
  selector: 'jhi-attendance-update',
  templateUrl: './attendance-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AttendanceUpdateComponent implements OnInit {
  isSaving = false;
  attendance: IAttendance | null = null;

  employeesSharedCollection: IEmployee[] = [];

  protected attendanceService = inject(AttendanceService);
  protected attendanceFormService = inject(AttendanceFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AttendanceFormGroup = this.attendanceFormService.createAttendanceFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendance }) => {
      this.attendance = attendance;
      if (attendance) {
        this.updateForm(attendance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attendance = this.attendanceFormService.getAttendance(this.editForm);
    if (attendance.id !== null) {
      this.subscribeToSaveResponse(this.attendanceService.update(attendance));
    } else {
      this.subscribeToSaveResponse(this.attendanceService.create(attendance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendance>>): void {
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

  protected updateForm(attendance: IAttendance): void {
    this.attendance = attendance;
    this.attendanceFormService.resetForm(this.editForm, attendance);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      attendance.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.attendance?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
