export interface IWage {
  id: number;
  coefficients?: number | null;
  baseSalary?: number | null;
  allowance?: number | null;
}

export type NewWage = Omit<IWage, 'id'> & { id: null };
