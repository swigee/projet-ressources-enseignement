package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO pour les semaines
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekDTO {
    private Integer num;
    private String date;
    private String type; // 'E' pour école, 'S' pour entreprise
}
