/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Formation;

public interface EducationManagerRepository extends JpaRepository<Formation, Integer> {
    
}
