package sae.project.dtos.vacataire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacataireDTO {
    private Integer id;

    // Step 1
    private String recruitmentManager;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String department;
    private String position;
    private String experience;
    private String profile;
    private String skills;

    // Step 2
    private Boolean advanceNotice;
    private String institution;
    private String site;
    private Boolean sentToManager;
    private String managerSignature;

    // Step 3
    private String knowledgeSource;
    private String otherKnowledgeSource;

    private String status;

    /** ID of the linked user account, null if no account has been created yet. */
    private Integer userId;

    /** True if a user account has been created and linked to this contractor profile. */
    private Boolean accountActive;
}
