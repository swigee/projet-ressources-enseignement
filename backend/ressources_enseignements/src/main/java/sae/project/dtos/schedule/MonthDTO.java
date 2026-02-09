package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO pour les mois
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthDTO {
    private String month;
    private List<WeekDTO> weeks;
}
