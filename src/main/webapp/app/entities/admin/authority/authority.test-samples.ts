import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '25a75f15-c8d3-479b-b922-0beaba7315e2',
};

export const sampleWithPartialData: IAuthority = {
  name: '23725b97-3f21-4d64-ad7b-57176b5b9343',
};

export const sampleWithFullData: IAuthority = {
  name: '3c6a7941-570d-4839-9241-79cd619f1bc8',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
