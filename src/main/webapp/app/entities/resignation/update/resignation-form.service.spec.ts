import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../resignation.test-samples';

import { ResignationFormService } from './resignation-form.service';

describe('Resignation Form Service', () => {
  let service: ResignationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResignationFormService);
  });

  describe('Service methods', () => {
    describe('createResignationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResignationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            submissionDate: expect.any(Object),
            effectiveDate: expect.any(Object),
            reason: expect.any(Object),
            status: expect.any(Object),
            notes: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });

      it('passing IResignation should create a new form with FormGroup', () => {
        const formGroup = service.createResignationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            submissionDate: expect.any(Object),
            effectiveDate: expect.any(Object),
            reason: expect.any(Object),
            status: expect.any(Object),
            notes: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });
    });

    describe('getResignation', () => {
      it('should return NewResignation for default Resignation initial value', () => {
        const formGroup = service.createResignationFormGroup(sampleWithNewData);

        const resignation = service.getResignation(formGroup) as any;

        expect(resignation).toMatchObject(sampleWithNewData);
      });

      it('should return NewResignation for empty Resignation initial value', () => {
        const formGroup = service.createResignationFormGroup();

        const resignation = service.getResignation(formGroup) as any;

        expect(resignation).toMatchObject({});
      });

      it('should return IResignation', () => {
        const formGroup = service.createResignationFormGroup(sampleWithRequiredData);

        const resignation = service.getResignation(formGroup) as any;

        expect(resignation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResignation should not enable id FormControl', () => {
        const formGroup = service.createResignationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResignation should disable id FormControl', () => {
        const formGroup = service.createResignationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
