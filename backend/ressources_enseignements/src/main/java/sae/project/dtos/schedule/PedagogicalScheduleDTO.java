package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO complet pour le planning
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedagogicalScheduleDTO {
    private String selectedYear;
    private String selectedClass;
    private String selectedSemester;
    private List<ResourceScheduleDTO> scheduleData;
    private ProjectScheduleDTO projectData;
    private List<MonthDTO> weeks;
    private ScheduleStatisticsDTO statistics;
}
