import { IContractType, NewContractType } from './contract-type.model';

export const sampleWithRequiredData: IContractType = {
  id: 19000,
  typeName: 'swift which',
};

export const sampleWithPartialData: IContractType = {
  id: 14792,
  typeName: 'yippee highly',
};

export const sampleWithFullData: IContractType = {
  id: 668,
  typeName: 'bah quintuple',
  description: 'since fund',
};

export const sampleWithNewData: NewContractType = {
  typeName: 'narrow',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
