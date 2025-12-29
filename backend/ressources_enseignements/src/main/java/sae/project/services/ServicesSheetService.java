package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.dtos.ServicesSummaryDto;
import sae.project.model.Assignment;
import sae.project.model.Ressources;
import sae.project.repositories.AssignmentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicesSheetService {
    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<ServicesSummaryDto> getServicesSummary(Integer userId) {
        // 1. Récupérer toutes les lignes brutes de la BDD pour ce prof
        List<Assignment> assignments = assignmentRepository.findByIduser_Iduser(userId);

        // 2. Regrouper par Ressource (Map <ID_Ressource, DTO>)
        Map<Integer, ServicesSummaryDto> summaryMap = new HashMap<>();

        for (Assignment assign : assignments) {
            Ressources ressource = assign.getIdressource();
            Integer resId = ressource.getIdressource();

            // Si on n'a pas encore cette ressource dans la liste, on la crée
            if (!summaryMap.containsKey(resId)) {
                ServicesSummaryDto dto = new ServicesSummaryDto();
                dto.setResourceTitle(ressource.getTitle());
                dto.setHoursCM(0);
                dto.setHoursTD(0);
                dto.setHoursTP(0);
                dto.setTotalHours(0);
                summaryMap.put(resId, dto);
            }


            ServicesSummaryDto currentDto = summaryMap.get(resId);
            int hours = assign.getAssignedtimes(); // 'assignedtimes' dans ton data.sql


            String type = assign.getLessontype();

            if ("CM".equalsIgnoreCase(type)) {
                currentDto.setHoursCM(currentDto.getHoursCM() + hours);
            } else if ("TD".equalsIgnoreCase(type)) {
                currentDto.setHoursTD(currentDto.getHoursTD() + hours);
            } else if ("TP".equalsIgnoreCase(type)) {
                currentDto.setHoursTP(currentDto.getHoursTP() + hours);
            }


            currentDto.setTotalHours(currentDto.getHoursCM() + currentDto.getHoursTD() + currentDto.getHoursTP());
        }


        return new ArrayList<>(summaryMap.values());
    }
}
