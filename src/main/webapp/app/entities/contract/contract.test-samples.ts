import dayjs from 'dayjs/esm';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 13332,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  status: 'DESTROY',
  contractCode: 'regarding sans presu',
};

export const sampleWithPartialData: IContract = {
  id: 19267,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  status: 'DESTROY',
  contractCode: 'hoarse since within',
};

export const sampleWithFullData: IContract = {
  id: 9193,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-18'),
  status: 'DESTROY',
  contractCode: 'before replacement w',
};

export const sampleWithNewData: NewContract = {
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  status: 'PENDING',
  contractCode: 'righteously sometime',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
