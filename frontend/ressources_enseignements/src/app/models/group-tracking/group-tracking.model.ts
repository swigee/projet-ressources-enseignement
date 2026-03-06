export interface GroupHours {
  actual: number;
  planned: number;
}

export interface GroupResourceRow {
  name: string;
  globalHours: GroupHours;
  tdHours: GroupHours;
  tpHours: GroupHours;
  cmHours: GroupHours;
}

export interface StudentGroup {
  id: string;          // ex: G1S1
  formation: string;
  year: string;
  semester: string;
  resources: GroupResourceRow[];
  totalGlobalHours: number;
  totalTdHours: number;
  totalTpHours: number;
  totalCmHours: number;
}

export interface GroupTrackingResponse {
  groups: StudentGroup[];
}

export interface Formation {
  id: string;
  name: string;
}
