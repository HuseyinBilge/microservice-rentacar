package com.kodlamaio.maintenanceservice.api.controller;


import com.kodlamaio.maintenanceservice.business.abstracts.MaintenanceService;
import com.kodlamaio.maintenanceservice.business.dto.request.CreateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.request.UpdateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.responce.CreateMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.GetAllMaintenancesResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.GetMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.UpdateMaintenanceResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenances")
public class MaintenancesController {
    private final MaintenanceService maintenanceService;

    @GetMapping
    public List<GetAllMaintenancesResponse> getAll() {
        return maintenanceService.getAll();
    }

    @GetMapping("{id}")
    public GetMaintenanceResponse getById(@PathVariable UUID id) {
        return maintenanceService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateMaintenanceResponse add(@RequestBody CreateMaintenanceRequest request) {
        return maintenanceService.add(request);
    }

    @PutMapping("{id}")
    public UpdateMaintenanceResponse update(@PathVariable UUID id, @RequestBody UpdateMaintenanceRequest request) {
        return maintenanceService.update(id, request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        maintenanceService.delete(id);
    }

    @PutMapping("/return")
    public GetMaintenanceResponse returnCarFromMaintenance(@RequestParam UUID id) {
        return maintenanceService.returnCarFromMaintenance(id);
    }
}