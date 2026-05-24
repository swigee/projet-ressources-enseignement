import { Component, inject, OnInit, signal } from '@angular/core';
import { PageTitle } from '../../services/page-title/page-title-service';
import { Auth } from '../../services/auth/auth';
import { CommonModule } from '@angular/common';
import { NotificationService, NotificationDTO } from '../../services/notification/notification.service';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './top-bar.html',
})
export class TopBar implements OnInit {
  pageTitleService = inject(PageTitle);
  authService = inject(Auth);
  notifService = inject(NotificationService);
  
  title = this.pageTitleService.title;
  readonly currentUser = signal<any>(null);

  notifications: NotificationDTO[] = [];
  unreadCount = 0;
  isNotificationMenuOpen = false;

  constructor() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser.set(user);
    });
  }

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      if (user && user.id) {
        this.loadNotifications(user.id);
      } else {
        this.notifications = [];
      }
    });
  }

  loadNotifications(userId: number) {
    this.notifService.getAllNotifications(userId).subscribe({
      next: (data: NotificationDTO[]) => {
        this.notifications = data;
        this.updateUnreadCount();
      },
      error: (err: any) => console.error('Erreur notifs', err)
    });
  }

  updateUnreadCount() {
    this.unreadCount = this.notifications.filter(n => !n.isRead).length;
  }

  toggleNotificationMenu() {
    this.isNotificationMenuOpen = !this.isNotificationMenuOpen;
  }

  markAsRead(notification: NotificationDTO) {
    if (notification.isRead) return;
    this.notifService.markAsRead(notification.id).subscribe({
      next: () => {
        notification.isRead = true;
        this.updateUnreadCount();
      },
      error: (err: any) => console.error('Erreur markAsRead', err)
    });
  }

  markAllAsRead() {
    const user = this.currentUser();
    if (user && user.id && this.unreadCount > 0) {
      this.notifService.markAllAsRead(user.id).subscribe({
        next: () => {
          this.notifications.forEach(n => n.isRead = true);
          this.updateUnreadCount();
        },
        error: (err: any) => console.error('Erreur markAll', err)
      });
    }
  }

  deleteNotification(notification: NotificationDTO) {
    this.notifService.deleteNotification(notification.id).subscribe({
      next: () => {
        this.notifications = this.notifications.filter(n => n.id !== notification.id);
        this.updateUnreadCount();
        if (this.notifications.length === 0) {
          this.isNotificationMenuOpen = false;
        }
      },
      error: (err: any) => console.error('Erreur delete', err)
    });
  }

  getRole() {
    if (this.currentUser() && this.currentUser().roleList && this.currentUser().roleList.length != 0) {
      return this.currentUser().roleList[0].name;
    }
    return "";
  }

  isConnected() {
    return this.currentUser() != null
  }

  logout() {
    this.authService.logout();
  }
}
