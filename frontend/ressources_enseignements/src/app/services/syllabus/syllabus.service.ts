import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable, catchError, throwError } from 'rxjs';
import { SyllabusResource } from '../../models/syllabus/syllabus.model';

@Injectable({
  providedIn: 'root'
})
export class SyllabusService {
  private apiUrl = `${environment.apiUrl}/api/syllabus`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<SyllabusResource[]> {
    return this.http.get<SyllabusResource[]>(this.apiUrl).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération du syllabus', error);
        return throwError(() => new Error('Erreur serveur'));
      })
    );
  }

  getById(id: number): Observable<SyllabusResource> {
    return this.http.get<SyllabusResource>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        if (error.status === 404) {
          return throwError(() => new Error('Ressource non trouvée'));
        }
        return throwError(() => new Error('Erreur serveur'));
      })
    );
  }

  uploadCsv(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.apiUrl}/upload-csv`, formData, { responseType: 'text' }).pipe(
      catchError(error => {
        console.error('Erreur lors de l\'upload du CSV', error);
        return throwError(() => new Error('Erreur lors de l\'importation du fichier CSV'));
      })
    );
  }

  updatePersonalFields(id: number, data: Partial<SyllabusResource>): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/personal-fields`, data).pipe(
      catchError(error => {
        console.error('Erreur lors de la mise à jour des champs personnels', error);
        return throwError(() => new Error('Erreur lors de la sauvegarde'));
      })
    );
  }

  /**
   * Déduit le semestre à partir du code de la ressource.
   * Ex: R1.01 -> S1, R2.01 -> S2, SAÉ 3.01 -> S3, R4.Real.08 -> S4, R5.Real.05 -> S5, R6.01 -> S6
   */
  static getSemesterFromCode(code: string): string {
    const cleanCode = code.trim().toUpperCase();

    // Pour les SAÉ : "SAÉ 1.01" ou "SAE 1.01" -> le chiffre après "SAE/SAÉ "
    const saeMatch = cleanCode.match(/^SA[É|E]\s+(\d)/);
    if (saeMatch) {
      return 'S' + saeMatch[1];
    }

    // Pour les Ressources : "R1.01", "R4.Real.08", "R5.A.04" -> le chiffre après "R"
    const rMatch = cleanCode.match(/^R(\d)/);
    if (rMatch) {
      return 'S' + rMatch[1];
    }

    return 'Inconnu';
  }

  /**
   * Déduit le parcours à partir du code de la ressource.
   * - R4.Real.08 / R5.Real.05 / SAÉ 3.Real.01 -> "RA" (Réalisation)
   * - R4.Deploi.08 / SAÉ 3.Deploi.01 -> "DACS" (Déploiement)
   * - R5.A.04 -> "AGED" (A = Administration, Gestion et Exploitation des Données)
   * - S1/S2 ou pas d'indicateur -> "Tronc Commun"
   */
  static getPathwayFromCode(code: string): string {
    const codeLower = code.toLowerCase();

    if (codeLower.includes('.real.') || codeLower.includes('.real ')) {
      return 'RA';
    }
    if (codeLower.includes('.deploi.') || codeLower.includes('.deploi ')) {
      return 'DACS';
    }
    if (codeLower.includes('.a.') || codeLower.includes('.a ')) {
      return 'AGED';
    }

    // S1 et S2 sont toujours tronc commun
    const semestre = SyllabusService.getSemesterFromCode(code);
    if (semestre === 'S1' || semestre === 'S2') {
      return 'Tronc Commun';
    }

    // S3+ sans indicateur de parcours = Tronc Commun aussi
    return 'Tronc Commun';
  }
}
