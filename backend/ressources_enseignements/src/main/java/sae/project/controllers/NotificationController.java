package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

import sae.project.model.Notification;
import sae.project.services.NotificationService;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/notifications")
@RestController
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;

    // Récupérer les notifications non lues d'un utilisateur
    @GetMapping("/unread/{userId}")
    public List<Notification> getUnreadNotifications(@PathVariable Integer userId) {
        return notificationService.getUnreadNotifications(userId);
    }

    // Marquer une notification spécifique comme lue
    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
    }
}
