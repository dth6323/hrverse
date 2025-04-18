import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

type EmployeeFormDefaults = Pick<NewEmployee, 'id'>;

type EmployeeFormGroupContent = {
  id: FormControl<IEmployee['id'] | NewEmployee['id']>;
  name: FormControl<IEmployee['name']>;
  phone: FormControl<IEmployee['phone']>;
  email: FormControl<IEmployee['email']>;
  address: FormControl<IEmployee['address']>;
  gender: FormControl<IEmployee['gender']>;
  dateOfBirth: FormControl<IEmployee['dateOfBirth']>;
  department: FormControl<IEmployee['department']>;
  contract: FormControl<IEmployee['contract']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = {
      ...this.getFormDefaults(),
      ...employee,
    };
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(employeeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      phone: new FormControl(employeeRawValue.phone, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      email: new FormControl(employeeRawValue.email, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      address: new FormControl(employeeRawValue.address, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      gender: new FormControl(employeeRawValue.gender, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(employeeRawValue.dateOfBirth, {
        validators: [Validators.required],
      }),
      department: new FormControl(employeeRawValue.department),
      contract: new FormControl(employeeRawValue.contract),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return form.getRawValue() as IEmployee | NewEmployee;
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = { ...this.getFormDefaults(), ...employee };
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    return {
      id: null,
    };
  }
}
