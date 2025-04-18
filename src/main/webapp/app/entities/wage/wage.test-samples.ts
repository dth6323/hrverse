import { IWage, NewWage } from './wage.model';

export const sampleWithRequiredData: IWage = {
  id: 18447,
  coefficients: 2194.68,
  baseSalary: 26189.25,
  allowance: 18953.48,
};

export const sampleWithPartialData: IWage = {
  id: 4937,
  coefficients: 29592.35,
  baseSalary: 11618.1,
  allowance: 31884.03,
};

export const sampleWithFullData: IWage = {
  id: 11268,
  coefficients: 14956.33,
  baseSalary: 26121.71,
  allowance: 17980.42,
};

export const sampleWithNewData: NewWage = {
  coefficients: 1393.27,
  baseSalary: 7131.02,
  allowance: 21487.46,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
