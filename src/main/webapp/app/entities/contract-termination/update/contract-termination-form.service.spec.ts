import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../contract-termination.test-samples';

import { ContractTerminationFormService } from './contract-termination-form.service';

describe('ContractTermination Form Service', () => {
  let service: ContractTerminationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContractTerminationFormService);
  });

  describe('Service methods', () => {
    describe('createContractTerminationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContractTerminationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            terminationDate: expect.any(Object),
            reason: expect.any(Object),
            compensation: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });

      it('passing IContractTermination should create a new form with FormGroup', () => {
        const formGroup = service.createContractTerminationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            terminationDate: expect.any(Object),
            reason: expect.any(Object),
            compensation: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });
    });

    describe('getContractTermination', () => {
      it('should return NewContractTermination for default ContractTermination initial value', () => {
        const formGroup = service.createContractTerminationFormGroup(sampleWithNewData);

        const contractTermination = service.getContractTermination(formGroup) as any;

        expect(contractTermination).toMatchObject(sampleWithNewData);
      });

      it('should return NewContractTermination for empty ContractTermination initial value', () => {
        const formGroup = service.createContractTerminationFormGroup();

        const contractTermination = service.getContractTermination(formGroup) as any;

        expect(contractTermination).toMatchObject({});
      });

      it('should return IContractTermination', () => {
        const formGroup = service.createContractTerminationFormGroup(sampleWithRequiredData);

        const contractTermination = service.getContractTermination(formGroup) as any;

        expect(contractTermination).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContractTermination should not enable id FormControl', () => {
        const formGroup = service.createContractTerminationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContractTermination should disable id FormControl', () => {
        const formGroup = service.createContractTerminationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
