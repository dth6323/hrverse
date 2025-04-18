export interface IDepartment {
  id: number;
  departmentName?: string | null;
  address?: string | null;
}

export type NewDepartment = Omit<IDepartment, 'id'> & { id: null };
