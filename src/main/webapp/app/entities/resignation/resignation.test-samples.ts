import dayjs from 'dayjs/esm';

import { IResignation, NewResignation } from './resignation.model';

export const sampleWithRequiredData: IResignation = {
  id: 9378,
  submissionDate: dayjs('2025-04-17'),
  effectiveDate: dayjs('2025-04-17'),
  reason: 'nippy',
  status: 'DESTROY',
};

export const sampleWithPartialData: IResignation = {
  id: 30579,
  submissionDate: dayjs('2025-04-17'),
  effectiveDate: dayjs('2025-04-17'),
  reason: 'a case',
  status: 'DESTROY',
  notes: 'palatable blink',
};

export const sampleWithFullData: IResignation = {
  id: 13968,
  submissionDate: dayjs('2025-04-17'),
  effectiveDate: dayjs('2025-04-17'),
  reason: 'after plan swing',
  status: 'PENDING',
  notes: 'feather',
};

export const sampleWithNewData: NewResignation = {
  submissionDate: dayjs('2025-04-17'),
  effectiveDate: dayjs('2025-04-18'),
  reason: 'topsail tedious rigid',
  status: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
