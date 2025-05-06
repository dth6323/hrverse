import dayjs from 'dayjs/esm';
import { IWage } from 'app/entities/wage/wage.model';
import { IContractType } from 'app/entities/contract-type/contract-type.model';

export interface IContract {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: string | null;
  contractCode?: string | null;
  wage?: IWage | null;
  contractType?: IContractType | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
