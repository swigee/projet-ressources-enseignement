import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from '../../../environments/environment';

export interface NotificationDTO {
    id: number;
    message: string;
    isRead: boolean;
    createdAt: string;
}

@Injectable({
    providedIn: 'root'
})
export class NotificationService {
    private apiUrl = `${environment.apiUrl}/api/notifications`;

    constructor(private http: HttpClient) {}

    
    getAllNotifications(userId: number): Observable<NotificationDTO[]> {
        return this.http.get<NotificationDTO[]>(`${this.apiUrl}/user/${userId}`);
    }

    markAsRead(notificationId: number): Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/${notificationId}/read`, {});
    }

    markAllAsRead(userId: number): Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/user/${userId}/read-all`, {});
    }

    deleteNotification(notificationId: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${notificationId}`);
    }
}
