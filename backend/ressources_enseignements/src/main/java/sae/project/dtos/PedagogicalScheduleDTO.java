package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO complet pour le planning
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedagogicalScheduleDTO {
    private String selectedYear;
    private String selectedClass;
    private java.util.List<ResourceScheduleDTO> scheduleData;
    private ProjectScheduleDTO projectData;
    private java.util.List<MonthDTO> weeks;
    private ScheduleStatisticsDTO statistics;
}
