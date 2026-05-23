export interface SoftwareModel {
  id?: number;
  name: string;
  version: string;
  plugins?: string;
  license: string;
  period: string;
  status: string;
  year?: string;
  resourceNames?: string[];
  userId?: number;
  userFullName?: string;
  userEmail?: string;
  resourceId?: number;
  resourceTitle?: string;
}
