import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAttendance, NewAttendance } from '../attendance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttendance for edit and NewAttendanceFormGroupInput for create.
 */
type AttendanceFormGroupInput = IAttendance | PartialWithRequiredKeyOf<NewAttendance>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAttendance | NewAttendance> = Omit<T, 'checkInTime' | 'checkOutTime'> & {
  checkInTime?: string | null;
  checkOutTime?: string | null;
};

type AttendanceFormRawValue = FormValueOf<IAttendance>;

type NewAttendanceFormRawValue = FormValueOf<NewAttendance>;

type AttendanceFormDefaults = Pick<NewAttendance, 'id' | 'checkInTime' | 'checkOutTime'>;

type AttendanceFormGroupContent = {
  id: FormControl<AttendanceFormRawValue['id'] | NewAttendance['id']>;
  dateOfwork: FormControl<AttendanceFormRawValue['dateOfwork']>;
  checkInTime: FormControl<AttendanceFormRawValue['checkInTime']>;
  checkOutTime: FormControl<AttendanceFormRawValue['checkOutTime']>;
  workHour: FormControl<AttendanceFormRawValue['workHour']>;
  employee: FormControl<AttendanceFormRawValue['employee']>;
};

export type AttendanceFormGroup = FormGroup<AttendanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttendanceFormService {
  createAttendanceFormGroup(attendance: AttendanceFormGroupInput = { id: null }): AttendanceFormGroup {
    const attendanceRawValue = this.convertAttendanceToAttendanceRawValue({
      ...this.getFormDefaults(),
      ...attendance,
    });
    return new FormGroup<AttendanceFormGroupContent>({
      id: new FormControl(
        { value: attendanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateOfwork: new FormControl(attendanceRawValue.dateOfwork, {
        validators: [Validators.required],
      }),
      checkInTime: new FormControl(attendanceRawValue.checkInTime, {
        validators: [Validators.required],
      }),
      checkOutTime: new FormControl(attendanceRawValue.checkOutTime, {
        validators: [Validators.required],
      }),
      workHour: new FormControl(attendanceRawValue.workHour, {
        validators: [Validators.required],
      }),
      employee: new FormControl(attendanceRawValue.employee),
    });
  }

  getAttendance(form: AttendanceFormGroup): IAttendance | NewAttendance {
    return this.convertAttendanceRawValueToAttendance(form.getRawValue() as AttendanceFormRawValue | NewAttendanceFormRawValue);
  }

  resetForm(form: AttendanceFormGroup, attendance: AttendanceFormGroupInput): void {
    const attendanceRawValue = this.convertAttendanceToAttendanceRawValue({ ...this.getFormDefaults(), ...attendance });
    form.reset(
      {
        ...attendanceRawValue,
        id: { value: attendanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AttendanceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      checkInTime: currentTime,
      checkOutTime: currentTime,
    };
  }

  private convertAttendanceRawValueToAttendance(
    rawAttendance: AttendanceFormRawValue | NewAttendanceFormRawValue,
  ): IAttendance | NewAttendance {
    return {
      ...rawAttendance,
      checkInTime: dayjs(rawAttendance.checkInTime, DATE_TIME_FORMAT),
      checkOutTime: dayjs(rawAttendance.checkOutTime, DATE_TIME_FORMAT),
    };
  }

  private convertAttendanceToAttendanceRawValue(
    attendance: IAttendance | (Partial<NewAttendance> & AttendanceFormDefaults),
  ): AttendanceFormRawValue | PartialWithRequiredKeyOf<NewAttendanceFormRawValue> {
    return {
      ...attendance,
      checkInTime: attendance.checkInTime ? attendance.checkInTime.format(DATE_TIME_FORMAT) : undefined,
      checkOutTime: attendance.checkOutTime ? attendance.checkOutTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
