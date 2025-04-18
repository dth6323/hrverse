import dayjs from 'dayjs/esm';

export interface ISalaryDistribute {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  workDay?: number | null;
  typeOfSalary?: string | null;
}

export type NewSalaryDistribute = Omit<ISalaryDistribute, 'id'> & { id: null };
