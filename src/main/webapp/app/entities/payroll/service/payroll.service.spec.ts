import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPayroll } from '../payroll.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../payroll.test-samples';

import { PayrollService } from './payroll.service';

const requireRestSample: IPayroll = {
  ...sampleWithRequiredData,
};

describe('Payroll Service', () => {
  let service: PayrollService;
  let httpMock: HttpTestingController;
  let expectedResult: IPayroll | IPayroll[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PayrollService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Payroll', () => {
      const payroll = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(payroll).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Payroll', () => {
      const payroll = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(payroll).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Payroll', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Payroll', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Payroll', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPayrollToCollectionIfMissing', () => {
      it('should add a Payroll to an empty array', () => {
        const payroll: IPayroll = sampleWithRequiredData;
        expectedResult = service.addPayrollToCollectionIfMissing([], payroll);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(payroll);
      });

      it('should not add a Payroll to an array that contains it', () => {
        const payroll: IPayroll = sampleWithRequiredData;
        const payrollCollection: IPayroll[] = [
          {
            ...payroll,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPayrollToCollectionIfMissing(payrollCollection, payroll);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Payroll to an array that doesn't contain it", () => {
        const payroll: IPayroll = sampleWithRequiredData;
        const payrollCollection: IPayroll[] = [sampleWithPartialData];
        expectedResult = service.addPayrollToCollectionIfMissing(payrollCollection, payroll);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(payroll);
      });

      it('should add only unique Payroll to an array', () => {
        const payrollArray: IPayroll[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const payrollCollection: IPayroll[] = [sampleWithRequiredData];
        expectedResult = service.addPayrollToCollectionIfMissing(payrollCollection, ...payrollArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const payroll: IPayroll = sampleWithRequiredData;
        const payroll2: IPayroll = sampleWithPartialData;
        expectedResult = service.addPayrollToCollectionIfMissing([], payroll, payroll2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(payroll);
        expect(expectedResult).toContain(payroll2);
      });

      it('should accept null and undefined values', () => {
        const payroll: IPayroll = sampleWithRequiredData;
        expectedResult = service.addPayrollToCollectionIfMissing([], null, payroll, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(payroll);
      });

      it('should return initial array if no Payroll is added', () => {
        const payrollCollection: IPayroll[] = [sampleWithRequiredData];
        expectedResult = service.addPayrollToCollectionIfMissing(payrollCollection, undefined, null);
        expect(expectedResult).toEqual(payrollCollection);
      });
    });

    describe('comparePayroll', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePayroll(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePayroll(entity1, entity2);
        const compareResult2 = service.comparePayroll(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePayroll(entity1, entity2);
        const compareResult2 = service.comparePayroll(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePayroll(entity1, entity2);
        const compareResult2 = service.comparePayroll(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
