import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../payroll.test-samples';

import { PayrollFormService } from './payroll-form.service';

describe('Payroll Form Service', () => {
  let service: PayrollFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PayrollFormService);
  });

  describe('Service methods', () => {
    describe('createPayrollFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPayrollFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            salary: expect.any(Object),
            workDay: expect.any(Object),
            employee: expect.any(Object),
            wage: expect.any(Object),
            salaryDistribute: expect.any(Object),
          }),
        );
      });

      it('passing IPayroll should create a new form with FormGroup', () => {
        const formGroup = service.createPayrollFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            salary: expect.any(Object),
            workDay: expect.any(Object),
            employee: expect.any(Object),
            wage: expect.any(Object),
            salaryDistribute: expect.any(Object),
          }),
        );
      });
    });

    describe('getPayroll', () => {
      it('should return NewPayroll for default Payroll initial value', () => {
        const formGroup = service.createPayrollFormGroup(sampleWithNewData);

        const payroll = service.getPayroll(formGroup) as any;

        expect(payroll).toMatchObject(sampleWithNewData);
      });

      it('should return NewPayroll for empty Payroll initial value', () => {
        const formGroup = service.createPayrollFormGroup();

        const payroll = service.getPayroll(formGroup) as any;

        expect(payroll).toMatchObject({});
      });

      it('should return IPayroll', () => {
        const formGroup = service.createPayrollFormGroup(sampleWithRequiredData);

        const payroll = service.getPayroll(formGroup) as any;

        expect(payroll).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPayroll should not enable id FormControl', () => {
        const formGroup = service.createPayrollFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPayroll should disable id FormControl', () => {
        const formGroup = service.createPayrollFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
