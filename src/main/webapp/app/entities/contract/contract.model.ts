import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IWage } from 'app/entities/wage/wage.model';
import { IContractType } from 'app/entities/contract-type/contract-type.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IContract {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof Status | null;
  contractCode?: string | null;
  employee?: IEmployee | null;
  wage?: IWage | null;
  contractType?: IContractType | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
