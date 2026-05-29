package sae.project.dtos.assignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import sae.project.dtos.teacher.TeacherDTO;

/** Full assignment grid DTO — contains all data needed to render the assignment page. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentGridDTO {
    private String selectedProgram;
    private String selectedYear;
    private String selectedSemester;
    private List<TeacherDTO> availableTeachers;
    private List<AssignmentRowDTO> assignmentGrid;
    private AssignmentStatisticsDTO statistics;
}
