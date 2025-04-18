import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IContractTermination } from '../contract-termination.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../contract-termination.test-samples';

import { ContractTerminationService, RestContractTermination } from './contract-termination.service';

const requireRestSample: RestContractTermination = {
  ...sampleWithRequiredData,
  terminationDate: sampleWithRequiredData.terminationDate?.format(DATE_FORMAT),
};

describe('ContractTermination Service', () => {
  let service: ContractTerminationService;
  let httpMock: HttpTestingController;
  let expectedResult: IContractTermination | IContractTermination[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContractTerminationService);
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

    it('should create a ContractTermination', () => {
      const contractTermination = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contractTermination).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContractTermination', () => {
      const contractTermination = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contractTermination).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContractTermination', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContractTermination', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContractTermination', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContractTerminationToCollectionIfMissing', () => {
      it('should add a ContractTermination to an empty array', () => {
        const contractTermination: IContractTermination = sampleWithRequiredData;
        expectedResult = service.addContractTerminationToCollectionIfMissing([], contractTermination);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractTermination);
      });

      it('should not add a ContractTermination to an array that contains it', () => {
        const contractTermination: IContractTermination = sampleWithRequiredData;
        const contractTerminationCollection: IContractTermination[] = [
          {
            ...contractTermination,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContractTerminationToCollectionIfMissing(contractTerminationCollection, contractTermination);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContractTermination to an array that doesn't contain it", () => {
        const contractTermination: IContractTermination = sampleWithRequiredData;
        const contractTerminationCollection: IContractTermination[] = [sampleWithPartialData];
        expectedResult = service.addContractTerminationToCollectionIfMissing(contractTerminationCollection, contractTermination);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractTermination);
      });

      it('should add only unique ContractTermination to an array', () => {
        const contractTerminationArray: IContractTermination[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contractTerminationCollection: IContractTermination[] = [sampleWithRequiredData];
        expectedResult = service.addContractTerminationToCollectionIfMissing(contractTerminationCollection, ...contractTerminationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contractTermination: IContractTermination = sampleWithRequiredData;
        const contractTermination2: IContractTermination = sampleWithPartialData;
        expectedResult = service.addContractTerminationToCollectionIfMissing([], contractTermination, contractTermination2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractTermination);
        expect(expectedResult).toContain(contractTermination2);
      });

      it('should accept null and undefined values', () => {
        const contractTermination: IContractTermination = sampleWithRequiredData;
        expectedResult = service.addContractTerminationToCollectionIfMissing([], null, contractTermination, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractTermination);
      });

      it('should return initial array if no ContractTermination is added', () => {
        const contractTerminationCollection: IContractTermination[] = [sampleWithRequiredData];
        expectedResult = service.addContractTerminationToCollectionIfMissing(contractTerminationCollection, undefined, null);
        expect(expectedResult).toEqual(contractTerminationCollection);
      });
    });

    describe('compareContractTermination', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContractTermination(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContractTermination(entity1, entity2);
        const compareResult2 = service.compareContractTermination(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContractTermination(entity1, entity2);
        const compareResult2 = service.compareContractTermination(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContractTermination(entity1, entity2);
        const compareResult2 = service.compareContractTermination(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
