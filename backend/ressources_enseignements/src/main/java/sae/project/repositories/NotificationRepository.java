package sae.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sae.project.model.Notification;

public interface NotificationRepository extends JpaRepository <Notification,Integer> {
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Integer userId);
    
} 
    

