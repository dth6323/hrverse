import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../wage.test-samples';

import { WageFormService } from './wage-form.service';

describe('Wage Form Service', () => {
  let service: WageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WageFormService);
  });

  describe('Service methods', () => {
    describe('createWageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            coefficients: expect.any(Object),
            baseSalary: expect.any(Object),
            allowance: expect.any(Object),
          }),
        );
      });

      it('passing IWage should create a new form with FormGroup', () => {
        const formGroup = service.createWageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            coefficients: expect.any(Object),
            baseSalary: expect.any(Object),
            allowance: expect.any(Object),
          }),
        );
      });
    });

    describe('getWage', () => {
      it('should return NewWage for default Wage initial value', () => {
        const formGroup = service.createWageFormGroup(sampleWithNewData);

        const wage = service.getWage(formGroup) as any;

        expect(wage).toMatchObject(sampleWithNewData);
      });

      it('should return NewWage for empty Wage initial value', () => {
        const formGroup = service.createWageFormGroup();

        const wage = service.getWage(formGroup) as any;

        expect(wage).toMatchObject({});
      });

      it('should return IWage', () => {
        const formGroup = service.createWageFormGroup(sampleWithRequiredData);

        const wage = service.getWage(formGroup) as any;

        expect(wage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWage should not enable id FormControl', () => {
        const formGroup = service.createWageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWage should disable id FormControl', () => {
        const formGroup = service.createWageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
