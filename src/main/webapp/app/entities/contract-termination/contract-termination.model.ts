import dayjs from 'dayjs/esm';
import { IContract } from 'app/entities/contract/contract.model';

export interface IContractTermination {
  id: number;
  terminationDate?: dayjs.Dayjs | null;
  reason?: string | null;
  compensation?: number | null;
  contract?: IContract | null;
}

export type NewContractTermination = Omit<IContractTermination, 'id'> & { id: null };
