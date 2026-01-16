package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sae.project.dtos.ServicesSummaryDto;
import sae.project.services.ServicesSheetService;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:4200")
public class ServicesSheetController {
    @Autowired
    private ServicesSheetService servicesSheetService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ServicesSummaryDto>> getMyServicesSheet(@PathVariable Integer userId) {
        List<ServicesSummaryDto> summary = servicesSheetService.getServicesSummary(userId);
        return ResponseEntity.ok(summary);
    }
}
