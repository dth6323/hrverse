import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IResignation {
  id: number;
  submissionDate?: dayjs.Dayjs | null;
  effectiveDate?: dayjs.Dayjs | null;
  reason?: string | null;
  status?: string | null;
  notes?: string | null;
  employee?: IEmployee | null;
}

export type NewResignation = Omit<IResignation, 'id'> & { id: null };
