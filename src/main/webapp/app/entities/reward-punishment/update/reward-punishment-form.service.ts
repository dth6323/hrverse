import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRewardPunishment, NewRewardPunishment } from '../reward-punishment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRewardPunishment for edit and NewRewardPunishmentFormGroupInput for create.
 */
type RewardPunishmentFormGroupInput = IRewardPunishment | PartialWithRequiredKeyOf<NewRewardPunishment>;

type RewardPunishmentFormDefaults = Pick<NewRewardPunishment, 'id'>;

type RewardPunishmentFormGroupContent = {
  id: FormControl<IRewardPunishment['id'] | NewRewardPunishment['id']>;
  type: FormControl<IRewardPunishment['type']>;
  amount: FormControl<IRewardPunishment['amount']>;
  reason: FormControl<IRewardPunishment['reason']>;
  applyDate: FormControl<IRewardPunishment['applyDate']>;
  notes: FormControl<IRewardPunishment['notes']>;
  employee: FormControl<IRewardPunishment['employee']>;
};

export type RewardPunishmentFormGroup = FormGroup<RewardPunishmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RewardPunishmentFormService {
  createRewardPunishmentFormGroup(rewardPunishment: RewardPunishmentFormGroupInput = { id: null }): RewardPunishmentFormGroup {
    const rewardPunishmentRawValue = {
      ...this.getFormDefaults(),
      ...rewardPunishment,
    };
    return new FormGroup<RewardPunishmentFormGroupContent>({
      id: new FormControl(
        { value: rewardPunishmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(rewardPunishmentRawValue.type, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      amount: new FormControl(rewardPunishmentRawValue.amount, {
        validators: [Validators.required],
      }),
      reason: new FormControl(rewardPunishmentRawValue.reason, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      applyDate: new FormControl(rewardPunishmentRawValue.applyDate, {
        validators: [Validators.required],
      }),
      notes: new FormControl(rewardPunishmentRawValue.notes, {
        validators: [Validators.maxLength(500)],
      }),
      employee: new FormControl(rewardPunishmentRawValue.employee),
    });
  }

  getRewardPunishment(form: RewardPunishmentFormGroup): IRewardPunishment | NewRewardPunishment {
    return form.getRawValue() as IRewardPunishment | NewRewardPunishment;
  }

  resetForm(form: RewardPunishmentFormGroup, rewardPunishment: RewardPunishmentFormGroupInput): void {
    const rewardPunishmentRawValue = { ...this.getFormDefaults(), ...rewardPunishment };
    form.reset(
      {
        ...rewardPunishmentRawValue,
        id: { value: rewardPunishmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RewardPunishmentFormDefaults {
    return {
      id: null,
    };
  }
}
