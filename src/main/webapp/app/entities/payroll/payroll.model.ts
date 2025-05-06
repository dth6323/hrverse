import { IEmployee } from 'app/entities/employee/employee.model';
import { ISalaryDistribute } from 'app/entities/salary-distribute/salary-distribute.model';

export interface IPayroll {
  id: number;
  salary?: number | null;
  workDay?: number | null;
  employee?: IEmployee | null;
  salaryDistribute?: ISalaryDistribute | null;
}

export type NewPayroll = Omit<IPayroll, 'id'> & { id: null };
