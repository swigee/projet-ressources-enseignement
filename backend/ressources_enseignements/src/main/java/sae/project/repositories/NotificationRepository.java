package sae.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

import sae.project.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    
    List<Notification> findByUserIdOrderByCreatedAtDesc(Integer userId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
    void markAllAsReadByUserId(@Param("userId") Integer userId);
}
    

