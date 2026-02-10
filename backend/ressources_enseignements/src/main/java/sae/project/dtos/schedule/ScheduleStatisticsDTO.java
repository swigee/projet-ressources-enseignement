package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// DTO pour les statistiques
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleStatisticsDTO {
    private Integer totalResources;
    private Integer totalWithProject;
    private Integer companyWeeksCount;
    private Map<Integer, Integer> weeklyTotals;
    private Map<Integer, Integer> weeklyTotalsWithProject;
}
