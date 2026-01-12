package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO pour les mois
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthDTO {
    private String month;
    private java.util.List<WeekDTO> weeks;
}
