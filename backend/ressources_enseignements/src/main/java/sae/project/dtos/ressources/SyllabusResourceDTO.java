package sae.project.dtos.ressources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusResourceDTO {
    private Integer id;
    private String code;
    private String title;
    private String description;
    private String knowledge;
    private String criticalLearning;
    private String officialVolume;
    private String personalDescription;
    private String personalKnowledge;
    private String personalLearning;
    private String personalVolume;
}
