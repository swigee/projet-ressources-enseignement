import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

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
    private apiUrl = 'http://localhost:8080/api/notifications';

    constructor(private http: HttpClient) {}

    getUnreadNotifications(userId: number): Observable<NotificationDTO[]> {
        return this.http.get<NotificationDTO[]>(`${this.apiUrl}/unread/${userId}`);
    }

    markAsRead(notificationId: number): Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/${notificationId}/read`, {});
    }
}
