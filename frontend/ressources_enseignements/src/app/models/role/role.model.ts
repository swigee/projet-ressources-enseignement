import { PermissionModel } from '../permission/permission.model';

export interface RoleModel {
  id: number;
  name: string;

  // backend: isActive / is_active selon mapping JSON
  isActive?: boolean | null;
  is_active?: number | boolean | null;

  colorHex?: string | null;
  slug?: string | null;

  // facultatif: si un jour on renvoie les permissions directement dans /roles
  permissions?: PermissionModel[];
}
