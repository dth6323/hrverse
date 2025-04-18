import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPayroll, NewPayroll } from '../payroll.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayroll for edit and NewPayrollFormGroupInput for create.
 */
type PayrollFormGroupInput = IPayroll | PartialWithRequiredKeyOf<NewPayroll>;

type PayrollFormDefaults = Pick<NewPayroll, 'id'>;

type PayrollFormGroupContent = {
  id: FormControl<IPayroll['id'] | NewPayroll['id']>;
  salary: FormControl<IPayroll['salary']>;
  workDay: FormControl<IPayroll['workDay']>;
  employee: FormControl<IPayroll['employee']>;
  wage: FormControl<IPayroll['wage']>;
  salaryDistribute: FormControl<IPayroll['salaryDistribute']>;
};

export type PayrollFormGroup = FormGroup<PayrollFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PayrollFormService {
  createPayrollFormGroup(payroll: PayrollFormGroupInput = { id: null }): PayrollFormGroup {
    const payrollRawValue = {
      ...this.getFormDefaults(),
      ...payroll,
    };
    return new FormGroup<PayrollFormGroupContent>({
      id: new FormControl(
        { value: payrollRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      salary: new FormControl(payrollRawValue.salary, {
        validators: [Validators.required],
      }),
      workDay: new FormControl(payrollRawValue.workDay, {
        validators: [Validators.required],
      }),
      employee: new FormControl(payrollRawValue.employee),
      wage: new FormControl(payrollRawValue.wage),
      salaryDistribute: new FormControl(payrollRawValue.salaryDistribute),
    });
  }

  getPayroll(form: PayrollFormGroup): IPayroll | NewPayroll {
    return form.getRawValue() as IPayroll | NewPayroll;
  }

  resetForm(form: PayrollFormGroup, payroll: PayrollFormGroupInput): void {
    const payrollRawValue = { ...this.getFormDefaults(), ...payroll };
    form.reset(
      {
        ...payrollRawValue,
        id: { value: payrollRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PayrollFormDefaults {
    return {
      id: null,
    };
  }
}
