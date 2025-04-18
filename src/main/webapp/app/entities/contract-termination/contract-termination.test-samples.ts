import dayjs from 'dayjs/esm';

import { IContractTermination, NewContractTermination } from './contract-termination.model';

export const sampleWithRequiredData: IContractTermination = {
  id: 21260,
  terminationDate: dayjs('2025-04-17'),
  reason: 'alongside',
};

export const sampleWithPartialData: IContractTermination = {
  id: 13375,
  terminationDate: dayjs('2025-04-17'),
  reason: 'mob',
};

export const sampleWithFullData: IContractTermination = {
  id: 11580,
  terminationDate: dayjs('2025-04-18'),
  reason: 'if aw ugh',
  compensation: 30803.09,
};

export const sampleWithNewData: NewContractTermination = {
  terminationDate: dayjs('2025-04-17'),
  reason: 'pfft jovially',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
