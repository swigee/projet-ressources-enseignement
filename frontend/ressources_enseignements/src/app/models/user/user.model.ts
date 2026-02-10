import { RoleModel } from '../role/role.model';

export interface UserModel {
  iduser: number;
  firstname?: string;
  lastname?: string;
  username: string;
  address?: string;
  email?: string;
  servicevalidation?: boolean;
  roles?: RoleModel[];
  formations?: any[];
  tickets?: any[];
  assignments?: any[];
}
