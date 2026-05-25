export interface VacataireModel {
  id?: number;
  // Étape 1 — Fiche de recrutement
  responsableRecrutement?: string;
  qualiteResponsable?: string;
  departement?: string;
  dateEntretien?: string;
  prenom?: string;
  nom?: string;
  formationVisee?: string;
  natureVacation?: string;
  profilTechnique?: string;
  profilPedagogique?: string;
  competences?: string;
  // Étape 2 — Autres
  siteBourgenBresse?: string;
  siteVilleurbanneDoua?: string;
  siteVilleurbanneGratteCiel?: string;
  transmisCV?: string;
  signatureResponsable?: string;
  // Étape 3 — Source de connaissance (valeurs séparées par virgule)
  sourceConnaissances?: string;
  sourceConnaissanceAutre?: string;
  // Admin
  statut?: string;
  /** ID of the linked user account — populated once statut becomes VALIDE. */
  userId?: number;
  /** True when a user account has been created for this contractor. */
  accountActive?: boolean;
}
