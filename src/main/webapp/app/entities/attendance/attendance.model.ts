import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IAttendance {
  id: number;
  dateOfwork?: dayjs.Dayjs | null;
  checkInTime?: dayjs.Dayjs | null;
  checkOutTime?: dayjs.Dayjs | null;
  workHour?: number | null;
  employee?: IEmployee | null;
}

export type NewAttendance = Omit<IAttendance, 'id'> & { id: null };
