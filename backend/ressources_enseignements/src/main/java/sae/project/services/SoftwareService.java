package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.dtos.software.SoftwareDto;
import sae.project.dtos.software.SoftwareResponseDto;
import sae.project.model.Resource;
import sae.project.model.Software;
import sae.project.model.User;
import sae.project.repositories.ResourceRepository;
import sae.project.repositories.SoftwareRepository;
import sae.project.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoftwareService {

    @Autowired
    private SoftwareRepository softwareRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    public List<SoftwareResponseDto> getAllSoftware() {
        return softwareRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<SoftwareResponseDto> getSoftwareByResource(Integer resourceId) {
        return softwareRepository.findByResourceId(resourceId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SoftwareResponseDto createSoftware(SoftwareDto dto) {
        User user = dto.userId != null ? userRepository.findById(dto.userId).orElse(null) : null;
        Resource resource = dto.resourceId != null ? resourceRepository.findById(dto.resourceId).orElse(null) : null;

        Software software = Software.builder()
                .name(dto.name)
                .version(dto.version)
                .plugins(dto.plugins)
                .license(dto.license)
                .period(dto.period)
                .status(dto.status != null ? dto.status : "REQUESTED")
                .year(dto.year)
                .resourceNames(dto.resourceNames)
                .user(user)
                .resource(resource)
                .build();

        return toDto(softwareRepository.save(software));
    }

    public SoftwareResponseDto updateSoftware(Integer id, SoftwareDto dto) {
        Software software = softwareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Software not found"));

        software.setName(dto.name);
        software.setVersion(dto.version);
        software.setPlugins(dto.plugins);
        software.setLicense(dto.license);
        software.setPeriod(dto.period);
        if (dto.status != null) software.setStatus(dto.status);
        if (dto.year != null) software.setYear(dto.year);
        software.setResourceNames(dto.resourceNames);

        if (dto.userId != null) {
            software.setUser(userRepository.findById(dto.userId).orElse(null));
        }
        if (dto.resourceId != null) {
            software.setResource(resourceRepository.findById(dto.resourceId).orElse(null));
        }

        return toDto(softwareRepository.save(software));
    }

    public void deleteSoftware(Integer id) {
        softwareRepository.deleteById(id);
    }

    public List<String> getAvailableResourceTitles() {
        return resourceRepository.findAll().stream()
                .map(r -> r.getTitle())
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private SoftwareResponseDto toDto(Software s) {
        List<String> names = (s.getResourceNames() != null && !s.getResourceNames().isBlank())
                ? Arrays.stream(s.getResourceNames().split(","))
                        .map(String::trim)
                        .filter(n -> !n.isEmpty())
                        .collect(Collectors.toList())
                : List.of();

        return new SoftwareResponseDto(
                s.getId(),
                s.getName(),
                s.getVersion(),
                s.getPlugins(),
                s.getLicense(),
                s.getPeriod(),
                s.getStatus(),
                s.getYear(),
                names,
                s.getUser() != null ? s.getUser().getId() : null,
                s.getUser() != null ? s.getUser().getFirstName() + " " + s.getUser().getLastName() : null,
                s.getUser() != null ? s.getUser().getEmail() : null,
                s.getResource() != null ? s.getResource().getId() : null,
                s.getResource() != null ? s.getResource().getTitle() : null
        );
    }
}
