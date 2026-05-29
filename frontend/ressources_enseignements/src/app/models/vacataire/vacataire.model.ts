export interface VacataireModel {
  id?: number;
  // Step 1 — Recruitment form
  recruitmentManager?: string;
  managerTitle?: string;
  department?: string;
  interviewDate?: string;
  firstName?: string;
  lastName?: string;
  targetFormation?: string;
  vacationType?: string;
  technicalProfile?: string;
  pedagogicalProfile?: string;
  skills?: string;
  // Step 2 — Other
  siteBourgenBresse?: string;
  siteVilleurbanneDoua?: string;
  siteVilleurbanneGratteCiel?: string;
  cvSubmitted?: string;
  managerSignature?: string;
  // Step 3 — Knowledge source (comma-separated values)
  knowledgeSource?: string;
  otherKnowledgeSource?: string;
  // Admin
  status?: string;
  /** ID of the linked user account — populated once status becomes VALIDATED. */
  userId?: number;
  /** True when a user account has been created for this contractor. */
  accountActive?: boolean;
}
