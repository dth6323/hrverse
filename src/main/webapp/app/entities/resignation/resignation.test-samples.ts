import dayjs from 'dayjs/esm';

import { IResignation, NewResignation } from './resignation.model';

export const sampleWithRequiredData: IResignation = {
  id: 9378,
  submissionDate: dayjs('2025-04-17'),
  effectiveDate: dayjs('2025-04-17'),
  reason: 'nippy',
  status: 'because repeatedly',
};

export const sampleWithPartialData: IResignation = {
  id: 12297,
  submissionDate: dayjs('2025-04-17'),
  effectiveDate: dayjs('2025-04-17'),
  reason: 'trim',
  status: 'disbar academics',
};

export const sampleWithFullData: IResignation = {
  id: 28700,
  submissionDate: dayjs('2025-04-17'),
  effectiveDate: dayjs('2025-04-17'),
  reason: 'since lest',
  status: 'since along royal',
  notes: 'metallic',
};

export const sampleWithNewData: NewResignation = {
  submissionDate: dayjs('2025-04-17'),
  effectiveDate: dayjs('2025-04-17'),
  reason: 'proliferate circa',
  status: 'phew disapprove',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
