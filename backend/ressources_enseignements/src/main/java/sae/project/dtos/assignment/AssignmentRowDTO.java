package sae.project.dtos.assignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import sae.project.dtos.teacher.TeacherAssignmentDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentRowDTO {
    private Integer resourceId;
    private String module;
    private List<String> groups;

    private Integer tdHours;
    private Integer tpHours;
    private Integer cmHours;

    private List<TeacherAssignmentDTO> tdTeachers;
    private List<TeacherAssignmentDTO> tpTeachers;
    private List<TeacherAssignmentDTO> cmTeachers;
}
