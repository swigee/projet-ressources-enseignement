import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, forkJoin } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { GroupTrackingResponse, StudentGroup, Formation } from '../../models/group-tracking/group-tracking.model';
import { RessourcesService } from '../ressources/ressources-service';
import { RessourceRow } from '../../models/ressources/ressources.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class GroupTrackingService {
  private readonly http = inject(HttpClient);
  private readonly ressourcesService = inject(RessourcesService);
  private readonly apiUrl = `${environment.apiUrl}/api/group-tracking`;

  // G1 et G2 correspondent aux class_name en base
  private readonly CLASSES = ['G1', 'G2', 'G3', 'G4'];

  getFormations(): Observable<Formation[]> {
    return this.http.get<Formation[]>(`${this.apiUrl}/formations`).pipe(
      catchError(() => of([
        { id: 'Informatique', name: 'BUT Informatique' },
        { id: 'Reseaux et Telecommunications', name: 'BUT R&T' },
        { id: 'Science des Donnees', name: 'BUT Science des Données' },
      ]))
    );
  }

  getGroups(formation: string, year: string, semester: string): Observable<GroupTrackingResponse> {
    const requests = this.CLASSES.map((className, i) =>
      this.ressourcesService.getRessourcesTable(year, className, semester, formation).pipe(
        map(response => this.mapToStudentGroup(response.ressources, i + 1, semester, formation, year)),
        catchError(() => of(null))
      )
    );

    return forkJoin(requests).pipe(
      map(results => ({
        groups: results.filter((g): g is StudentGroup => g !== null && g.resources.length > 0)
      }))
    );
  }

  private mapToStudentGroup(
    rows: RessourceRow[], index: number, semester: string,
    formation: string, year: string
  ): StudentGroup {
    const resources = rows.map(row => {
      const actualHours = row.assignedTeachers.reduce((sum, t) => sum + t.assignedHours, 0);
      return {
        name: row.moduleName,
        globalHours: { actual: actualHours,   planned: row.plannedHours },
        tdHours:     { actual: 0,             planned: row.tdHours },
        tpHours:     { actual: 0,             planned: row.tpHours },
        cmHours:     { actual: 0,             planned: row.cmHours },
      };
    });

    return {
      id: `G${index}S${semester}`,
      formation, year, semester,
      resources,
      totalGlobalHours: resources.reduce((s, r) => s + r.globalHours.actual, 0),
      totalTdHours:     rows.reduce((s, r) => s + r.tdHours,     0),
      totalTpHours:     rows.reduce((s, r) => s + r.tpHours,     0),
      totalCmHours:     rows.reduce((s, r) => s + r.cmHours,     0),
    };
  }
}
