import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContractTermination, NewContractTermination } from '../contract-termination.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContractTermination for edit and NewContractTerminationFormGroupInput for create.
 */
type ContractTerminationFormGroupInput = IContractTermination | PartialWithRequiredKeyOf<NewContractTermination>;

type ContractTerminationFormDefaults = Pick<NewContractTermination, 'id'>;

type ContractTerminationFormGroupContent = {
  id: FormControl<IContractTermination['id'] | NewContractTermination['id']>;
  terminationDate: FormControl<IContractTermination['terminationDate']>;
  reason: FormControl<IContractTermination['reason']>;
  compensation: FormControl<IContractTermination['compensation']>;
  contract: FormControl<IContractTermination['contract']>;
};

export type ContractTerminationFormGroup = FormGroup<ContractTerminationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractTerminationFormService {
  createContractTerminationFormGroup(contractTermination: ContractTerminationFormGroupInput = { id: null }): ContractTerminationFormGroup {
    const contractTerminationRawValue = {
      ...this.getFormDefaults(),
      ...contractTermination,
    };
    return new FormGroup<ContractTerminationFormGroupContent>({
      id: new FormControl(
        { value: contractTerminationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      terminationDate: new FormControl(contractTerminationRawValue.terminationDate, {
        validators: [Validators.required],
      }),
      reason: new FormControl(contractTerminationRawValue.reason, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      compensation: new FormControl(contractTerminationRawValue.compensation),
      contract: new FormControl(contractTerminationRawValue.contract),
    });
  }

  getContractTermination(form: ContractTerminationFormGroup): IContractTermination | NewContractTermination {
    return form.getRawValue() as IContractTermination | NewContractTermination;
  }

  resetForm(form: ContractTerminationFormGroup, contractTermination: ContractTerminationFormGroupInput): void {
    const contractTerminationRawValue = { ...this.getFormDefaults(), ...contractTermination };
    form.reset(
      {
        ...contractTerminationRawValue,
        id: { value: contractTerminationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractTerminationFormDefaults {
    return {
      id: null,
    };
  }
}
