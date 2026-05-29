
package sae.project.dtos.education;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sae.project.model.Formation;
import sae.project.model.Resource;
import sae.project.model.Semester;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDTO {
    private Formation program;
    private List<Semester> semesters;
    private List<Resource> resources;
}

