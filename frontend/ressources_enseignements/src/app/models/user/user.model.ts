import { RoleModel } from '../role/role.model';

export interface UserModel {
  iduser: number;
  firstname?: string;
  lastname?: string;
  username: string;
  address?: string;
  email?: string;
  validationStatus?: string;
  roles?: RoleModel[];
  programs?: any[];
  assignments?: any[];
}
