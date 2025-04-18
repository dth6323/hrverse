import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IResignation } from '../resignation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../resignation.test-samples';

import { ResignationService, RestResignation } from './resignation.service';

const requireRestSample: RestResignation = {
  ...sampleWithRequiredData,
  submissionDate: sampleWithRequiredData.submissionDate?.format(DATE_FORMAT),
  effectiveDate: sampleWithRequiredData.effectiveDate?.format(DATE_FORMAT),
};

describe('Resignation Service', () => {
  let service: ResignationService;
  let httpMock: HttpTestingController;
  let expectedResult: IResignation | IResignation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ResignationService);
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

    it('should create a Resignation', () => {
      const resignation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(resignation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Resignation', () => {
      const resignation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(resignation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Resignation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Resignation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Resignation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addResignationToCollectionIfMissing', () => {
      it('should add a Resignation to an empty array', () => {
        const resignation: IResignation = sampleWithRequiredData;
        expectedResult = service.addResignationToCollectionIfMissing([], resignation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resignation);
      });

      it('should not add a Resignation to an array that contains it', () => {
        const resignation: IResignation = sampleWithRequiredData;
        const resignationCollection: IResignation[] = [
          {
            ...resignation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addResignationToCollectionIfMissing(resignationCollection, resignation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Resignation to an array that doesn't contain it", () => {
        const resignation: IResignation = sampleWithRequiredData;
        const resignationCollection: IResignation[] = [sampleWithPartialData];
        expectedResult = service.addResignationToCollectionIfMissing(resignationCollection, resignation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resignation);
      });

      it('should add only unique Resignation to an array', () => {
        const resignationArray: IResignation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const resignationCollection: IResignation[] = [sampleWithRequiredData];
        expectedResult = service.addResignationToCollectionIfMissing(resignationCollection, ...resignationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resignation: IResignation = sampleWithRequiredData;
        const resignation2: IResignation = sampleWithPartialData;
        expectedResult = service.addResignationToCollectionIfMissing([], resignation, resignation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resignation);
        expect(expectedResult).toContain(resignation2);
      });

      it('should accept null and undefined values', () => {
        const resignation: IResignation = sampleWithRequiredData;
        expectedResult = service.addResignationToCollectionIfMissing([], null, resignation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resignation);
      });

      it('should return initial array if no Resignation is added', () => {
        const resignationCollection: IResignation[] = [sampleWithRequiredData];
        expectedResult = service.addResignationToCollectionIfMissing(resignationCollection, undefined, null);
        expect(expectedResult).toEqual(resignationCollection);
      });
    });

    describe('compareResignation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareResignation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareResignation(entity1, entity2);
        const compareResult2 = service.compareResignation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareResignation(entity1, entity2);
        const compareResult2 = service.compareResignation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareResignation(entity1, entity2);
        const compareResult2 = service.compareResignation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
