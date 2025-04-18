import dayjs from 'dayjs/esm';

import { ISalaryDistribute, NewSalaryDistribute } from './salary-distribute.model';

export const sampleWithRequiredData: ISalaryDistribute = {
  id: 13077,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-18'),
  workDay: 20216,
  typeOfSalary: 'psst highly insist',
};

export const sampleWithPartialData: ISalaryDistribute = {
  id: 27931,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  workDay: 22098,
  typeOfSalary: 'rightfully',
};

export const sampleWithFullData: ISalaryDistribute = {
  id: 6401,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  workDay: 22398,
  typeOfSalary: 'incomparable',
};

export const sampleWithNewData: NewSalaryDistribute = {
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  workDay: 929,
  typeOfSalary: 'sediment absentmindedly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
