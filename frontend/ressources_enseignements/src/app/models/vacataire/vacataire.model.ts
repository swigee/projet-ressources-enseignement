export interface VacataireModel {
  id?: number;
  responsableRecrutement?: string;
  prenom?: string;
  nom?: string;
  dateNaissance?: string;
  departement?: string;
  fonction?: string;
  experience?: string;
  profil?: string;
  competences?: string;
  vueEnAmont?: boolean;
  etablissement?: string;
  site?: string;
  transmisResponsable?: boolean;
  signatureResponsable?: string;
  sourceConnaissance?: string;
  sourceConnaissanceAutre?: string;
  statut?: string;
}
