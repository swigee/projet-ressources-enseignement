import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GroupTrackingService } from '../../services/group-tracking/group-tracking.service';
import { StudentGroup, Formation, GroupResourceRow } from '../../models/group-tracking/group-tracking.model';

@Component({
  selector: 'app-group-tracking',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './group-tracking.html',
})
export class GroupTracking implements OnInit {
  private readonly service = inject(GroupTrackingService);

  readonly PAGE_SIZE = 5;

  formations = signal<Formation[]>([]);
  selectedFormation = signal<string>('Informatique');
  selectedYear = signal<string>('1');
  // semester est toujours '1' ou '2' (relatif à l'année) — la DB stocke 1 ou 2 pour toutes les années
  selectedSemester = signal<string>('1');
  searchQuery = signal<string>('');

  groups = signal<StudentGroup[]>([]);
  isLoading = signal<boolean>(false);

  expandedGroups = signal<Set<string>>(new Set());
  currentPages = signal<Map<string, number>>(new Map());

  filteredGroups = computed(() => {
    const q = this.searchQuery().toLowerCase().trim();
    if (!q) return this.groups();
    return this.groups().filter(g =>
      g.id.toLowerCase().includes(q) ||
      g.resources.some(r => r.name.toLowerCase().includes(q))
    );
  });

  ngOnInit(): void {
    this.service.getFormations().subscribe(f => this.formations.set(f));
    this.loadGroups();
  }

  loadGroups(): void {
    this.isLoading.set(true);
    this.expandedGroups.set(new Set());
    this.currentPages.set(new Map());

    this.service.getGroups(
      this.selectedFormation(),
      this.selectedYear(),
      this.selectedSemester()
    ).subscribe({
      next: (res) => {
        this.groups.set(res.groups);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  onYearChange(year: string): void {
    this.selectedYear.set(year);
    this.selectedSemester.set('1');
    this.loadGroups();
  }

  isExpanded(groupId: string): boolean {
    return this.expandedGroups().has(groupId);
  }

  toggleGroup(groupId: string): void {
    const set = new Set(this.expandedGroups());
    if (set.has(groupId)) {
      set.delete(groupId);
    } else {
      set.add(groupId);
      if (!this.currentPages().has(groupId)) {
        const map = new Map(this.currentPages());
        map.set(groupId, 1);
        this.currentPages.set(map);
      }
    }
    this.expandedGroups.set(set);
  }

  getPage(group: StudentGroup): GroupResourceRow[] {
    const page = this.currentPages().get(group.id) ?? 1;
    const start = (page - 1) * this.PAGE_SIZE;
    return group.resources.slice(start, start + this.PAGE_SIZE);
  }

  getTotalPages(group: StudentGroup): number {
    return Math.ceil(group.resources.length / this.PAGE_SIZE);
  }

  getPageNumbers(group: StudentGroup): number[] {
    return Array.from({ length: this.getTotalPages(group) }, (_, i) => i + 1);
  }

  getCurrentPage(groupId: string): number {
    return this.currentPages().get(groupId) ?? 1;
  }

  goToPage(groupId: string, page: number): void {
    const map = new Map(this.currentPages());
    map.set(groupId, page);
    this.currentPages.set(map);
  }

  allGroupsCollapsed(): boolean {
    const groups = this.filteredGroups();
    return groups.length > 0 && !groups.some(g => this.isExpanded(g.id));
  }

  formatHours(h: { actual: number; planned: number }): string {
    return `${h.actual}h/${h.planned}h`;
  }

  getYears(): string[] { return ['1', '2', '3']; }
  getSemesters(): string[] { return ['1', '2']; }
}
