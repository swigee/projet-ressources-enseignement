package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sae.project.model.Notification;
import sae.project.model.User;
import sae.project.repositories.NotificationRepository;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    
    // Récupérer les notifs non lues d'un utilisateur
    public List<Notification> getUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    // Marquer une notification comme "Lue"
    public void markAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable avec l'ID: " + notificationId));
        
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    // Créer une nouvelle notification pour un utilisateur
    public void createNotification(User user, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .isRead(false)
                .createdAt(new Date())
                .build();
                
        notificationRepository.save(notification);
    }
}
