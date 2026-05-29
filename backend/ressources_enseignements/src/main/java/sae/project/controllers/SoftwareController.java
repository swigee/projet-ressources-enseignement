package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.software.SoftwareDto;
import sae.project.dtos.software.SoftwareResponseDto;
import sae.project.services.SoftwareService;

import java.util.List;

@RestController
@RequestMapping("/api/software")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Software", description = "API for managing software linked to resources")
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;

    @GetMapping
    @Operation(summary = "Get all software entries")
    public List<SoftwareResponseDto> getAllSoftware() {
        return softwareService.getAllSoftware();
    }

    @GetMapping("/resource/{resourceId}")
    @Operation(summary = "Get software entries linked to a specific resource")
    public List<SoftwareResponseDto> getSoftwareByResource(@PathVariable Integer resourceId) {
        return softwareService.getSoftwareByResource(resourceId);
    }

    @PostMapping
    @Operation(summary = "Create a new software entry")
    public SoftwareResponseDto createSoftware(@RequestBody SoftwareDto dto) {
        return softwareService.createSoftware(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a software entry by ID")
    public SoftwareResponseDto updateSoftware(@PathVariable Integer id, @RequestBody SoftwareDto dto) {
        return softwareService.updateSoftware(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a software entry by ID")
    public void deleteSoftware(@PathVariable Integer id) {
        softwareService.deleteSoftware(id);
    }

    @GetMapping("/resources/titles")
    @Operation(summary = "Get the list of resource titles that have software linked")
    public List<String> getAvailableResourceTitles() {
        return softwareService.getAvailableResourceTitles();
    }
}
