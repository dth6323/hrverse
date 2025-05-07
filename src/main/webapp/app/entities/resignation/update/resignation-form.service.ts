import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IResignation, NewResignation } from '../resignation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResignation for edit and NewResignationFormGroupInput for create.
 */
type ResignationFormGroupInput = IResignation | PartialWithRequiredKeyOf<NewResignation>;

type ResignationFormDefaults = Pick<NewResignation, 'id'>;

type ResignationFormGroupContent = {
  id: FormControl<IResignation['id'] | NewResignation['id']>;
  submissionDate: FormControl<IResignation['submissionDate']>;
  effectiveDate: FormControl<IResignation['effectiveDate']>;
  reason: FormControl<IResignation['reason']>;
  status: FormControl<IResignation['status']>;
  notes: FormControl<IResignation['notes']>;
  employee: FormControl<IResignation['employee']>;
};

export type ResignationFormGroup = FormGroup<ResignationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResignationFormService {
  createResignationFormGroup(resignation: ResignationFormGroupInput = { id: null }): ResignationFormGroup {
    const resignationRawValue = {
      ...this.getFormDefaults(),
      ...resignation,
    };
    return new FormGroup<ResignationFormGroupContent>({
      id: new FormControl(
        { value: resignationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      submissionDate: new FormControl(resignationRawValue.submissionDate, {
        validators: [Validators.required],
      }),
      effectiveDate: new FormControl(resignationRawValue.effectiveDate, {
        validators: [Validators.required],
      }),
      reason: new FormControl(resignationRawValue.reason, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      status: new FormControl(resignationRawValue.status, {
        validators: [Validators.required],
      }),
      notes: new FormControl(resignationRawValue.notes, {
        validators: [Validators.maxLength(500)],
      }),
      employee: new FormControl(resignationRawValue.employee),
    });
  }

  getResignation(form: ResignationFormGroup): IResignation | NewResignation {
    return form.getRawValue() as IResignation | NewResignation;
  }

  resetForm(form: ResignationFormGroup, resignation: ResignationFormGroupInput): void {
    const resignationRawValue = { ...this.getFormDefaults(), ...resignation };
    form.reset(
      {
        ...resignationRawValue,
        id: { value: resignationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResignationFormDefaults {
    return {
      id: null,
    };
  }
}
