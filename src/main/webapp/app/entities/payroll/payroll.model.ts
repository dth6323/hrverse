import { IEmployee } from 'app/entities/employee/employee.model';
import { IWage } from 'app/entities/wage/wage.model';
import { ISalaryDistribute } from 'app/entities/salary-distribute/salary-distribute.model';

export interface IPayroll {
  id: number;
  salary?: number | null;
  workDay?: number | null;
  employee?: IEmployee | null;
  wage?: IWage | null;
  salaryDistribute?: ISalaryDistribute | null;
}

export type NewPayroll = Omit<IPayroll, 'id'> & { id: null };
