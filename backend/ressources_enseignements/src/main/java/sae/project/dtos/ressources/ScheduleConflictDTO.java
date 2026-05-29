package sae.project.dtos.ressources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** DTO representing a scheduling conflict for a teacher. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleConflictDTO {
    private Integer teacherId;
    private String teacherName;
    private List<String> conflictingModules;
    private Integer weekNumber;
    private String timeSlot;
}
