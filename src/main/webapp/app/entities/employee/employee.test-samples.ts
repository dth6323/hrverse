import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 19147,
  name: 'imaginary legal',
  phone: '(533) 687-0999 x4048',
  email: 'Abigale4@gmail.com',
  address: 'justly',
  gender: 29134,
  dateOfBirth: dayjs('2025-04-17'),
};

export const sampleWithPartialData: IEmployee = {
  id: 21322,
  name: 'incinerate wear',
  phone: '639-419-6814 x72491',
  email: 'Duncan.Johns@yahoo.com',
  address: 'since inexperienced scent',
  gender: 17459,
  dateOfBirth: dayjs('2025-04-17'),
};

export const sampleWithFullData: IEmployee = {
  id: 17507,
  name: 'blend bakeware',
  phone: '1-420-963-5184 x5215',
  email: 'Lydia_Legros@yahoo.com',
  address: 'softly lyre',
  gender: 19879,
  dateOfBirth: dayjs('2025-04-17'),
};

export const sampleWithNewData: NewEmployee = {
  name: 'how collaborate',
  phone: '(557) 870-1454 x8416',
  email: 'Abdullah_Wehner86@hotmail.com',
  address: 'however',
  gender: 17213,
  dateOfBirth: dayjs('2025-04-18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
