import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../salary-distribute.test-samples';

import { SalaryDistributeFormService } from './salary-distribute-form.service';

describe('SalaryDistribute Form Service', () => {
  let service: SalaryDistributeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SalaryDistributeFormService);
  });

  describe('Service methods', () => {
    describe('createSalaryDistributeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalaryDistributeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            workDay: expect.any(Object),
            typeOfSalary: expect.any(Object),
          }),
        );
      });

      it('passing ISalaryDistribute should create a new form with FormGroup', () => {
        const formGroup = service.createSalaryDistributeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            workDay: expect.any(Object),
            typeOfSalary: expect.any(Object),
          }),
        );
      });
    });

    describe('getSalaryDistribute', () => {
      it('should return NewSalaryDistribute for default SalaryDistribute initial value', () => {
        const formGroup = service.createSalaryDistributeFormGroup(sampleWithNewData);

        const salaryDistribute = service.getSalaryDistribute(formGroup) as any;

        expect(salaryDistribute).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalaryDistribute for empty SalaryDistribute initial value', () => {
        const formGroup = service.createSalaryDistributeFormGroup();

        const salaryDistribute = service.getSalaryDistribute(formGroup) as any;

        expect(salaryDistribute).toMatchObject({});
      });

      it('should return ISalaryDistribute', () => {
        const formGroup = service.createSalaryDistributeFormGroup(sampleWithRequiredData);

        const salaryDistribute = service.getSalaryDistribute(formGroup) as any;

        expect(salaryDistribute).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalaryDistribute should not enable id FormControl', () => {
        const formGroup = service.createSalaryDistributeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalaryDistribute should disable id FormControl', () => {
        const formGroup = service.createSalaryDistributeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
