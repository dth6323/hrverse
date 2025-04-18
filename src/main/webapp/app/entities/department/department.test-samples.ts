import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 18739,
  departmentName: 'whether',
  address: 'developing veto settler',
};

export const sampleWithPartialData: IDepartment = {
  id: 12303,
  departmentName: 'victoriously notwithstanding godfather',
  address: 'aha amidst',
};

export const sampleWithFullData: IDepartment = {
  id: 821,
  departmentName: 'gah however',
  address: 'petty',
};

export const sampleWithNewData: NewDepartment = {
  departmentName: 'dependable highly except',
  address: 'impact',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
