import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IRewardPunishment {
  id: number;
  type?: string | null;
  amount?: number | null;
  reason?: string | null;
  applyDate?: dayjs.Dayjs | null;
  notes?: string | null;
  employee?: IEmployee | null;
}

export type NewRewardPunishment = Omit<IRewardPunishment, 'id'> & { id: null };
