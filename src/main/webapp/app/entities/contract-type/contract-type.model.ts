export interface IContractType {
  id: number;
  typeName?: string | null;
  description?: string | null;
}

export type NewContractType = Omit<IContractType, 'id'> & { id: null };
