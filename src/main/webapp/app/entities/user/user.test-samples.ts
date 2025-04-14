import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 3688,
  login: '6RlsuE',
};

export const sampleWithPartialData: IUser = {
  id: 23177,
  login: 'A2}6@i9qC\\j0\\)O\\55',
};

export const sampleWithFullData: IUser = {
  id: 19339,
  login: 'xBh4fA@G1\\F96lac\\uYJ9g',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
