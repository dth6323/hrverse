import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISalaryDistribute, NewSalaryDistribute } from '../salary-distribute.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalaryDistribute for edit and NewSalaryDistributeFormGroupInput for create.
 */
type SalaryDistributeFormGroupInput = ISalaryDistribute | PartialWithRequiredKeyOf<NewSalaryDistribute>;

type SalaryDistributeFormDefaults = Pick<NewSalaryDistribute, 'id'>;

type SalaryDistributeFormGroupContent = {
  id: FormControl<ISalaryDistribute['id'] | NewSalaryDistribute['id']>;
  startDate: FormControl<ISalaryDistribute['startDate']>;
  endDate: FormControl<ISalaryDistribute['endDate']>;
  workDay: FormControl<ISalaryDistribute['workDay']>;
  typeOfSalary: FormControl<ISalaryDistribute['typeOfSalary']>;
};

export type SalaryDistributeFormGroup = FormGroup<SalaryDistributeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SalaryDistributeFormService {
  createSalaryDistributeFormGroup(salaryDistribute: SalaryDistributeFormGroupInput = { id: null }): SalaryDistributeFormGroup {
    const salaryDistributeRawValue = {
      ...this.getFormDefaults(),
      ...salaryDistribute,
    };
    return new FormGroup<SalaryDistributeFormGroupContent>({
      id: new FormControl(
        { value: salaryDistributeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startDate: new FormControl(salaryDistributeRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(salaryDistributeRawValue.endDate, {
        validators: [Validators.required],
      }),
      workDay: new FormControl(salaryDistributeRawValue.workDay, {
        validators: [Validators.required],
      }),
      typeOfSalary: new FormControl(salaryDistributeRawValue.typeOfSalary, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
    });
  }

  getSalaryDistribute(form: SalaryDistributeFormGroup): ISalaryDistribute | NewSalaryDistribute {
    return form.getRawValue() as ISalaryDistribute | NewSalaryDistribute;
  }

  resetForm(form: SalaryDistributeFormGroup, salaryDistribute: SalaryDistributeFormGroupInput): void {
    const salaryDistributeRawValue = { ...this.getFormDefaults(), ...salaryDistribute };
    form.reset(
      {
        ...salaryDistributeRawValue,
        id: { value: salaryDistributeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SalaryDistributeFormDefaults {
    return {
      id: null,
    };
  }
}
