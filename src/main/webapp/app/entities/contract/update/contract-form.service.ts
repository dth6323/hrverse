import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContract, NewContract } from '../contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContract for edit and NewContractFormGroupInput for create.
 */
type ContractFormGroupInput = IContract | PartialWithRequiredKeyOf<NewContract>;

type ContractFormDefaults = Pick<NewContract, 'id'>;

type ContractFormGroupContent = {
  id: FormControl<IContract['id'] | NewContract['id']>;
  startDate: FormControl<IContract['startDate']>;
  endDate: FormControl<IContract['endDate']>;
  status: FormControl<IContract['status']>;
  contractCode: FormControl<IContract['contractCode']>;
  employee: FormControl<IContract['employee']>;
  wage: FormControl<IContract['wage']>;
  contractType: FormControl<IContract['contractType']>;
};

export type ContractFormGroup = FormGroup<ContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractFormService {
  createContractFormGroup(contract: ContractFormGroupInput = { id: null }): ContractFormGroup {
    const contractRawValue = {
      ...this.getFormDefaults(),
      ...contract,
    };
    return new FormGroup<ContractFormGroupContent>({
      id: new FormControl(
        { value: contractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startDate: new FormControl(contractRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(contractRawValue.endDate, {
        validators: [Validators.required],
      }),
      status: new FormControl(contractRawValue.status, {
        validators: [Validators.required],
      }),
      contractCode: new FormControl(contractRawValue.contractCode, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      employee: new FormControl(contractRawValue.employee),
      wage: new FormControl(contractRawValue.wage),
      contractType: new FormControl(contractRawValue.contractType),
    });
  }

  getContract(form: ContractFormGroup): IContract | NewContract {
    return form.getRawValue() as IContract | NewContract;
  }

  resetForm(form: ContractFormGroup, contract: ContractFormGroupInput): void {
    const contractRawValue = { ...this.getFormDefaults(), ...contract };
    form.reset(
      {
        ...contractRawValue,
        id: { value: contractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractFormDefaults {
    return {
      id: null,
    };
  }
}
