import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reward-punishment.test-samples';

import { RewardPunishmentFormService } from './reward-punishment-form.service';

describe('RewardPunishment Form Service', () => {
  let service: RewardPunishmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RewardPunishmentFormService);
  });

  describe('Service methods', () => {
    describe('createRewardPunishmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRewardPunishmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            amount: expect.any(Object),
            reason: expect.any(Object),
            applyDate: expect.any(Object),
            notes: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });

      it('passing IRewardPunishment should create a new form with FormGroup', () => {
        const formGroup = service.createRewardPunishmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            amount: expect.any(Object),
            reason: expect.any(Object),
            applyDate: expect.any(Object),
            notes: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });
    });

    describe('getRewardPunishment', () => {
      it('should return NewRewardPunishment for default RewardPunishment initial value', () => {
        const formGroup = service.createRewardPunishmentFormGroup(sampleWithNewData);

        const rewardPunishment = service.getRewardPunishment(formGroup) as any;

        expect(rewardPunishment).toMatchObject(sampleWithNewData);
      });

      it('should return NewRewardPunishment for empty RewardPunishment initial value', () => {
        const formGroup = service.createRewardPunishmentFormGroup();

        const rewardPunishment = service.getRewardPunishment(formGroup) as any;

        expect(rewardPunishment).toMatchObject({});
      });

      it('should return IRewardPunishment', () => {
        const formGroup = service.createRewardPunishmentFormGroup(sampleWithRequiredData);

        const rewardPunishment = service.getRewardPunishment(formGroup) as any;

        expect(rewardPunishment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRewardPunishment should not enable id FormControl', () => {
        const formGroup = service.createRewardPunishmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRewardPunishment should disable id FormControl', () => {
        const formGroup = service.createRewardPunishmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
