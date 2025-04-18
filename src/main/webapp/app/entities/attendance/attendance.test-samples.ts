import dayjs from 'dayjs/esm';

import { IAttendance, NewAttendance } from './attendance.model';

export const sampleWithRequiredData: IAttendance = {
  id: 24640,
  dateOfwork: dayjs('2025-04-17'),
  checkInTime: dayjs('2025-04-18T00:22'),
  checkOutTime: dayjs('2025-04-17T04:55'),
  workHour: 15091.53,
};

export const sampleWithPartialData: IAttendance = {
  id: 31773,
  dateOfwork: dayjs('2025-04-17'),
  checkInTime: dayjs('2025-04-17T11:05'),
  checkOutTime: dayjs('2025-04-17T22:02'),
  workHour: 25154.51,
};

export const sampleWithFullData: IAttendance = {
  id: 13303,
  dateOfwork: dayjs('2025-04-17'),
  checkInTime: dayjs('2025-04-17T06:34'),
  checkOutTime: dayjs('2025-04-17T04:21'),
  workHour: 31547.31,
};

export const sampleWithNewData: NewAttendance = {
  dateOfwork: dayjs('2025-04-17'),
  checkInTime: dayjs('2025-04-18T00:57'),
  checkOutTime: dayjs('2025-04-17T18:29'),
  workHour: 3521.41,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
