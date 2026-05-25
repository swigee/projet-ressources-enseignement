package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sae.project.dtos.ressources.SyllabusResourceDTO;
import sae.project.model.Resource;
import sae.project.repositories.ResourceRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SyllabusService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Transactional
    public void importSyllabusCsv(MultipartFile file) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                // Remove BOM if present
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                // Skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Parse CSV line separated by ';'
                String[] columns = line.split(";", -1);
                
                if (columns.length >= 6) {
                    String code = columns[0].trim();
                    String title = columns[1].trim();
                    String description = columns[2].trim();
                    String savoirs = columns[3].trim();
                    String ac = columns[4].trim();
                    String volume = columns[5].trim();

                    if (code.isEmpty() && title.isEmpty()) continue;

                    // IMPORTANT: Logic to merge with existing data
                    // 1. Try to find by exact Code (category)
                    Optional<Resource> existingResourceOpt = resourceRepository.findByCategory(code);
                    
                    // 2. If not found, try by Title (some existing data might use titles as IDs)
                    if (existingResourceOpt.isEmpty() && !title.isEmpty()) {
                        existingResourceOpt = resourceRepository.findByTitle(title);
                    }

                    Resource resource;
                    if (existingResourceOpt.isPresent()) {
                        resource = existingResourceOpt.get();
                        // If we found it by title but the code was different, update the code
                        resource.setCategory(code); 
                    } else {
                        resource = new Resource();
                        resource.setCategory(code);
                    }

                    // Update ONLY National Program fields from CSV
                    resource.setTitle(title);
                    resource.setDescription(description);
                    resource.setSavoirs(savoirs);
                    resource.setApprentissagesCritiques(ac);
                    resource.setVolumeOfficiel(volume);

                    // Note: We DO NOT touch 'personalDescription', 'personalSavoirs', etc.
                    // We DO NOT touch 'formationList' or 'assignmentList'
                    // This preserves all existing database relations.

                    resourceRepository.save(resource);
                }
            }
        }
    }

    public List<SyllabusResourceDTO> getAllSyllabusResources() {
        return resourceRepository.findAll().stream()
                .filter(r -> r.getCategory() != null && !r.getCategory().isEmpty())
                .filter(r -> isSyllabusCode(r.getCategory()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Verifie si le code correspond a un code du programme national.
     * Codes valides : R1.01, R2.05, SAE 1.01, SAÉ 3.Real.01, etc.
     */
    private boolean isSyllabusCode(String category) {
        if (category == null) return false;
        String cat = category.trim().toUpperCase();
        
        // Match R1, R2, etc.
        if (cat.startsWith("R") && cat.length() >= 2 && Character.isDigit(cat.charAt(1))) {
            return true;
        }
        // Match SAE or SAÉ
        if (cat.startsWith("SAE") || cat.startsWith("SA\u00c9")) {
            return true;
        }
        return false;
    }

    public SyllabusResourceDTO getSyllabusResourceByCode(String code) {
        return resourceRepository.findByCategory(code)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public SyllabusResourceDTO getSyllabusResourceById(Integer id) {
        return resourceRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Transactional
    public void updatePersonalFields(Integer id, SyllabusResourceDTO dto) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        resource.setPersonalDescription(dto.getPersonalDescription());
        resource.setPersonalSavoirs(dto.getPersonalSavoirs());
        resource.setPersonalApprentissages(dto.getPersonalApprentissages());
        resource.setPersonalVolume(dto.getPersonalVolume());
        resourceRepository.save(resource);
    }

    private SyllabusResourceDTO mapToDTO(Resource resource) {
        return SyllabusResourceDTO.builder()
                .id(resource.getId())
                .code(resource.getCategory())
                .title(resource.getTitle())
                .description(resource.getDescription())
                .savoirs(resource.getSavoirs())
                .apprentissagesCritiques(resource.getApprentissagesCritiques())
                .volumeOfficiel(resource.getVolumeOfficiel())
                .personalDescription(resource.getPersonalDescription())
                .personalSavoirs(resource.getPersonalSavoirs())
                .personalApprentissages(resource.getPersonalApprentissages())
                .personalVolume(resource.getPersonalVolume())
                .build();
    }
}
