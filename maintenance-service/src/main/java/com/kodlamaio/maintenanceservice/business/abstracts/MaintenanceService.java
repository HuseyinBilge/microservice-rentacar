package com.kodlamaio.maintenanceservice.business.abstracts;


import com.kodlamaio.maintenanceservice.business.dto.request.CreateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.request.UpdateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.responce.CreateMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.GetAllMaintenancesResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.GetMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.UpdateMaintenanceResponse;

import java.util.List;
import java.util.UUID;

public interface MaintenanceService {
    List<GetAllMaintenancesResponse> getAll();

    GetMaintenanceResponse getById(UUID id);

    GetMaintenanceResponse returnCarFromMaintenance(UUID id);

    CreateMaintenanceResponse add(CreateMaintenanceRequest request);

    UpdateMaintenanceResponse update(UUID id, UpdateMaintenanceRequest request);

    void delete(UUID id);
}
