import dayjs from 'dayjs/esm';
import { IDepartment } from 'app/entities/department/department.model';
import { IContract } from 'app/entities/contract/contract.model';

export interface IEmployee {
  id: number;
  name?: string | null;
  phone?: string | null;
  email?: string | null;
  address?: string | null;
  gender?: number | null;
  dateOfBirth?: dayjs.Dayjs | null;
  department?: IDepartment | null;
  contract?: IContract | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
