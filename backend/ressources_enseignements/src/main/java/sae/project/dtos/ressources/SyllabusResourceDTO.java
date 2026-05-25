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
    private String savoirs;
    private String apprentissagesCritiques;
    private String volumeOfficiel;
    private String personalDescription;
    private String personalSavoirs;
    private String personalApprentissages;
    private String personalVolume;
}
