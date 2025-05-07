import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 32043,
  name: 'fantastic hm',
  phone: '1-687-299-9404 x870',
  email: 'Lester.Murazik81@gmail.com',
  address: 'while',
  gender: 'MALE',
  dateOfBirth: dayjs('2025-04-17'),
};

export const sampleWithPartialData: IEmployee = {
  id: 15817,
  name: 'violently scholarship',
  phone: '492-396-8147 x2491',
  email: 'Duncan.Johns@yahoo.com',
  address: 'since inexperienced scent',
  gender: 'FEMALE',
  dateOfBirth: dayjs('2025-04-17'),
};

export const sampleWithFullData: IEmployee = {
  id: 17507,
  name: 'blend bakeware',
  phone: '1-420-963-5184 x5215',
  email: 'Lydia_Legros@yahoo.com',
  address: 'softly lyre',
  gender: 'FEMALE',
  dateOfBirth: dayjs('2025-04-17'),
};

export const sampleWithNewData: NewEmployee = {
  name: 'how collaborate',
  phone: '(557) 870-1454 x8416',
  email: 'Abdullah_Wehner86@hotmail.com',
  address: 'however',
  gender: 'FEMALE',
  dateOfBirth: dayjs('2025-04-18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
