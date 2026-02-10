package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekHoursDTO {
    @Builder.Default
    private Integer cm = 0;
    @Builder.Default
    private Integer td = 0;
    @Builder.Default
    private Integer tp = 0;
    @Builder.Default
    private Integer total = 0;
}
