package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.software.SoftwareDto;
import sae.project.dtos.software.SoftwareResponseDto;
import sae.project.services.SoftwareService;

import java.util.List;

@RestController
@RequestMapping("/api/software")
@CrossOrigin(origins = "http://localhost:4200")
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;

    @GetMapping
    public List<SoftwareResponseDto> getAllSoftware() {
        return softwareService.getAllSoftware();
    }

    @GetMapping("/resource/{resourceId}")
    public List<SoftwareResponseDto> getSoftwareByResource(@PathVariable Integer resourceId) {
        return softwareService.getSoftwareByResource(resourceId);
    }

    @PostMapping
    public SoftwareResponseDto createSoftware(@RequestBody SoftwareDto dto) {
        return softwareService.createSoftware(dto);
    }

    @PutMapping("/{id}")
    public SoftwareResponseDto updateSoftware(@PathVariable Integer id, @RequestBody SoftwareDto dto) {
        return softwareService.updateSoftware(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteSoftware(@PathVariable Integer id) {
        softwareService.deleteSoftware(id);
    }

    @GetMapping("/resources/titles")
    public List<String> getAvailableResourceTitles() {
        return softwareService.getAvailableResourceTitles();
    }
}
