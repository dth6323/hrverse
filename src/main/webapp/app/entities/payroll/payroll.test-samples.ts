import { IPayroll, NewPayroll } from './payroll.model';

export const sampleWithRequiredData: IPayroll = {
  id: 6266,
  salary: 31858,
  workDay: 12701,
};

export const sampleWithPartialData: IPayroll = {
  id: 15677,
  salary: 23021,
  workDay: 3783,
};

export const sampleWithFullData: IPayroll = {
  id: 18333,
  salary: 9345,
  workDay: 29188,
};

export const sampleWithNewData: NewPayroll = {
  salary: 28584,
  workDay: 4316,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
