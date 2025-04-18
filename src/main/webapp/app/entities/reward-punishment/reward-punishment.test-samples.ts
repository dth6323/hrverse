import dayjs from 'dayjs/esm';

import { IRewardPunishment, NewRewardPunishment } from './reward-punishment.model';

export const sampleWithRequiredData: IRewardPunishment = {
  id: 13068,
  type: 'minus phew',
  amount: 19880.18,
  reason: 'quicker after',
  applyDate: dayjs('2025-04-17'),
};

export const sampleWithPartialData: IRewardPunishment = {
  id: 31381,
  type: 'hence eek modulo',
  amount: 30616.34,
  reason: 'throughout humble',
  applyDate: dayjs('2025-04-17'),
  notes: 'waltz accentuate chairperson',
};

export const sampleWithFullData: IRewardPunishment = {
  id: 8329,
  type: 'likely fervently bec',
  amount: 10968.89,
  reason: 'oof recklessly',
  applyDate: dayjs('2025-04-17'),
  notes: 'and throughout supposing',
};

export const sampleWithNewData: NewRewardPunishment = {
  type: 'until quarrelsomely ',
  amount: 15604.16,
  reason: 'which modulo weary',
  applyDate: dayjs('2025-04-18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
