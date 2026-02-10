export interface RoleModel {
  idrole: number;
  title: string;
  rights: string;
}

export interface UserModel {
  iduser: number;
  firstname?: string;
  lastname?: string;
  username: string;
  address?: string;
  email?: string;
  validationStatus?: string;
  roles?: RoleModel[];
  formations?: any[];
  tickets?: any[];
  assignments?: any[];
}
