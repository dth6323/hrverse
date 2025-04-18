import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISalaryDistribute } from '../salary-distribute.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../salary-distribute.test-samples';

import { RestSalaryDistribute, SalaryDistributeService } from './salary-distribute.service';

const requireRestSample: RestSalaryDistribute = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('SalaryDistribute Service', () => {
  let service: SalaryDistributeService;
  let httpMock: HttpTestingController;
  let expectedResult: ISalaryDistribute | ISalaryDistribute[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SalaryDistributeService);
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

    it('should create a SalaryDistribute', () => {
      const salaryDistribute = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(salaryDistribute).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SalaryDistribute', () => {
      const salaryDistribute = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(salaryDistribute).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SalaryDistribute', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SalaryDistribute', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SalaryDistribute', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSalaryDistributeToCollectionIfMissing', () => {
      it('should add a SalaryDistribute to an empty array', () => {
        const salaryDistribute: ISalaryDistribute = sampleWithRequiredData;
        expectedResult = service.addSalaryDistributeToCollectionIfMissing([], salaryDistribute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryDistribute);
      });

      it('should not add a SalaryDistribute to an array that contains it', () => {
        const salaryDistribute: ISalaryDistribute = sampleWithRequiredData;
        const salaryDistributeCollection: ISalaryDistribute[] = [
          {
            ...salaryDistribute,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalaryDistributeToCollectionIfMissing(salaryDistributeCollection, salaryDistribute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SalaryDistribute to an array that doesn't contain it", () => {
        const salaryDistribute: ISalaryDistribute = sampleWithRequiredData;
        const salaryDistributeCollection: ISalaryDistribute[] = [sampleWithPartialData];
        expectedResult = service.addSalaryDistributeToCollectionIfMissing(salaryDistributeCollection, salaryDistribute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryDistribute);
      });

      it('should add only unique SalaryDistribute to an array', () => {
        const salaryDistributeArray: ISalaryDistribute[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salaryDistributeCollection: ISalaryDistribute[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryDistributeToCollectionIfMissing(salaryDistributeCollection, ...salaryDistributeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salaryDistribute: ISalaryDistribute = sampleWithRequiredData;
        const salaryDistribute2: ISalaryDistribute = sampleWithPartialData;
        expectedResult = service.addSalaryDistributeToCollectionIfMissing([], salaryDistribute, salaryDistribute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salaryDistribute);
        expect(expectedResult).toContain(salaryDistribute2);
      });

      it('should accept null and undefined values', () => {
        const salaryDistribute: ISalaryDistribute = sampleWithRequiredData;
        expectedResult = service.addSalaryDistributeToCollectionIfMissing([], null, salaryDistribute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salaryDistribute);
      });

      it('should return initial array if no SalaryDistribute is added', () => {
        const salaryDistributeCollection: ISalaryDistribute[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryDistributeToCollectionIfMissing(salaryDistributeCollection, undefined, null);
        expect(expectedResult).toEqual(salaryDistributeCollection);
      });
    });

    describe('compareSalaryDistribute', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSalaryDistribute(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSalaryDistribute(entity1, entity2);
        const compareResult2 = service.compareSalaryDistribute(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSalaryDistribute(entity1, entity2);
        const compareResult2 = service.compareSalaryDistribute(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSalaryDistribute(entity1, entity2);
        const compareResult2 = service.compareSalaryDistribute(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
