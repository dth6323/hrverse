import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IDepartment } from 'app/entities/department/department.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IEmployee {
  id: number;
  name?: string | null;
  phone?: string | null;
  email?: string | null;
  address?: string | null;
  gender?: keyof typeof Gender | null;
  dateOfBirth?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  department?: IDepartment | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
