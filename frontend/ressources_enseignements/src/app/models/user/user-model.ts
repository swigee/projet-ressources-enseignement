export interface UserModel {
  iduser?: number;
  firstname?: string;
  lastname?: string;
  username: string;
  address?: string;
  email?: string;
  servicevalidation?: boolean;

  formationList?: any[];
  roleList?: any[];
  ticketsList?: any[];
  assignmentList?: any[];
}

