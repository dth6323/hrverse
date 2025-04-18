import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWage, NewWage } from '../wage.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWage for edit and NewWageFormGroupInput for create.
 */
type WageFormGroupInput = IWage | PartialWithRequiredKeyOf<NewWage>;

type WageFormDefaults = Pick<NewWage, 'id'>;

type WageFormGroupContent = {
  id: FormControl<IWage['id'] | NewWage['id']>;
  coefficients: FormControl<IWage['coefficients']>;
  baseSalary: FormControl<IWage['baseSalary']>;
  allowance: FormControl<IWage['allowance']>;
};

export type WageFormGroup = FormGroup<WageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WageFormService {
  createWageFormGroup(wage: WageFormGroupInput = { id: null }): WageFormGroup {
    const wageRawValue = {
      ...this.getFormDefaults(),
      ...wage,
    };
    return new FormGroup<WageFormGroupContent>({
      id: new FormControl(
        { value: wageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      coefficients: new FormControl(wageRawValue.coefficients, {
        validators: [Validators.required],
      }),
      baseSalary: new FormControl(wageRawValue.baseSalary, {
        validators: [Validators.required],
      }),
      allowance: new FormControl(wageRawValue.allowance, {
        validators: [Validators.required],
      }),
    });
  }

  getWage(form: WageFormGroup): IWage | NewWage {
    return form.getRawValue() as IWage | NewWage;
  }

  resetForm(form: WageFormGroup, wage: WageFormGroupInput): void {
    const wageRawValue = { ...this.getFormDefaults(), ...wage };
    form.reset(
      {
        ...wageRawValue,
        id: { value: wageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WageFormDefaults {
    return {
      id: null,
    };
  }
}
