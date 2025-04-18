import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRewardPunishment } from '../reward-punishment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../reward-punishment.test-samples';

import { RestRewardPunishment, RewardPunishmentService } from './reward-punishment.service';

const requireRestSample: RestRewardPunishment = {
  ...sampleWithRequiredData,
  applyDate: sampleWithRequiredData.applyDate?.format(DATE_FORMAT),
};

describe('RewardPunishment Service', () => {
  let service: RewardPunishmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IRewardPunishment | IRewardPunishment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RewardPunishmentService);
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

    it('should create a RewardPunishment', () => {
      const rewardPunishment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rewardPunishment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RewardPunishment', () => {
      const rewardPunishment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rewardPunishment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RewardPunishment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RewardPunishment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RewardPunishment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRewardPunishmentToCollectionIfMissing', () => {
      it('should add a RewardPunishment to an empty array', () => {
        const rewardPunishment: IRewardPunishment = sampleWithRequiredData;
        expectedResult = service.addRewardPunishmentToCollectionIfMissing([], rewardPunishment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rewardPunishment);
      });

      it('should not add a RewardPunishment to an array that contains it', () => {
        const rewardPunishment: IRewardPunishment = sampleWithRequiredData;
        const rewardPunishmentCollection: IRewardPunishment[] = [
          {
            ...rewardPunishment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRewardPunishmentToCollectionIfMissing(rewardPunishmentCollection, rewardPunishment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RewardPunishment to an array that doesn't contain it", () => {
        const rewardPunishment: IRewardPunishment = sampleWithRequiredData;
        const rewardPunishmentCollection: IRewardPunishment[] = [sampleWithPartialData];
        expectedResult = service.addRewardPunishmentToCollectionIfMissing(rewardPunishmentCollection, rewardPunishment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rewardPunishment);
      });

      it('should add only unique RewardPunishment to an array', () => {
        const rewardPunishmentArray: IRewardPunishment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rewardPunishmentCollection: IRewardPunishment[] = [sampleWithRequiredData];
        expectedResult = service.addRewardPunishmentToCollectionIfMissing(rewardPunishmentCollection, ...rewardPunishmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rewardPunishment: IRewardPunishment = sampleWithRequiredData;
        const rewardPunishment2: IRewardPunishment = sampleWithPartialData;
        expectedResult = service.addRewardPunishmentToCollectionIfMissing([], rewardPunishment, rewardPunishment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rewardPunishment);
        expect(expectedResult).toContain(rewardPunishment2);
      });

      it('should accept null and undefined values', () => {
        const rewardPunishment: IRewardPunishment = sampleWithRequiredData;
        expectedResult = service.addRewardPunishmentToCollectionIfMissing([], null, rewardPunishment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rewardPunishment);
      });

      it('should return initial array if no RewardPunishment is added', () => {
        const rewardPunishmentCollection: IRewardPunishment[] = [sampleWithRequiredData];
        expectedResult = service.addRewardPunishmentToCollectionIfMissing(rewardPunishmentCollection, undefined, null);
        expect(expectedResult).toEqual(rewardPunishmentCollection);
      });
    });

    describe('compareRewardPunishment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRewardPunishment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRewardPunishment(entity1, entity2);
        const compareResult2 = service.compareRewardPunishment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRewardPunishment(entity1, entity2);
        const compareResult2 = service.compareRewardPunishment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRewardPunishment(entity1, entity2);
        const compareResult2 = service.compareRewardPunishment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
