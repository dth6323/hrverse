import dayjs from 'dayjs/esm';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 13332,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  status: 'nor reboot',
  contractCode: 'presume surprisingly',
};

export const sampleWithPartialData: IContract = {
  id: 23558,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  status: 'scrap',
  contractCode: 'galvanize helplessly',
};

export const sampleWithFullData: IContract = {
  id: 11762,
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-18'),
  status: 'hard-to-find pro',
  contractCode: 'tributary provided m',
};

export const sampleWithNewData: NewContract = {
  startDate: dayjs('2025-04-17'),
  endDate: dayjs('2025-04-17'),
  status: 'yum',
  contractCode: 'ha painfully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
