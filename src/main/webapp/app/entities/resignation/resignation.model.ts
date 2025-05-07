import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IResignation {
  id: number;
  submissionDate?: dayjs.Dayjs | null;
  effectiveDate?: dayjs.Dayjs | null;
  reason?: string | null;
  status?: keyof typeof Status | null;
  notes?: string | null;
  employee?: IEmployee | null;
}

export type NewResignation = Omit<IResignation, 'id'> & { id: null };
