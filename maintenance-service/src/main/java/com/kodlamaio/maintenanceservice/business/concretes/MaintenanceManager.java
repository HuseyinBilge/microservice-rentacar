package com.kodlamaio.maintenanceservice.business.concretes;

import com.kodlamaio.commonpackage.events.maintenance.MaintenanceCreatedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceDeletedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceReturnedEvent;

import com.kodlamaio.commonpackage.producer.KafkaProducer;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;

import com.kodlamaio.maintenanceservice.business.abstracts.MaintenanceService;
import com.kodlamaio.maintenanceservice.business.dto.request.CreateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.request.UpdateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.responce.CreateMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.GetAllMaintenancesResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.GetMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responce.UpdateMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.rules.MaintenanceBusinessRules;
import com.kodlamaio.maintenanceservice.entities.Maintenance;
import com.kodlamaio.maintenanceservice.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService {

    private final MaintenanceRepository repository;
    private final ModelMapperService mapper;
    private final KafkaProducer kafkaProducer;
    private final MaintenanceBusinessRules rules;

    @Override
    public List<GetAllMaintenancesResponse> getAll() {
        var maintenances = repository.findAll();
        var response = maintenances
                .stream()
                .map(maintenance -> mapper.forResponse().map(maintenance, GetAllMaintenancesResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetMaintenanceResponse getById(UUID id) {
        rules.checkIfMaintenanceExists(id);
        var maintenance = repository.findById(id).orElseThrow();
        var response = mapper.forResponse().map(maintenance, GetMaintenanceResponse.class);

        return response;
    }

    @Override
    public GetMaintenanceResponse returnCarFromMaintenance(UUID id) {
        Maintenance maintenance = repository.findById(id).orElseThrow();
        maintenance.setCompleted(true);
        maintenance.setEndDate(LocalDateTime.now());
        repository.save(maintenance);
        kafkaProducer.sendMessage(new MaintenanceReturnedEvent(maintenance.getCarId()),
                "maintenance-returned");
        var response = mapper.forResponse().map(maintenance, GetMaintenanceResponse.class);
        return response;
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) {
        rules.checkIfCarAvailable(request.getCarId());
        var maintenance = mapper.forRequest().map(request, Maintenance.class);
        maintenance.setId(null);
        maintenance.setStartDate(LocalDateTime.now());
        maintenance.setCompleted(false);
        maintenance.setEndDate(null);
        repository.save(maintenance);
        var response = mapper.forResponse().map(maintenance, CreateMaintenanceResponse.class);
        kafkaProducer.sendMessage(new MaintenanceCreatedEvent(maintenance.getCarId()), "maintenance-created");


        return response;
    }

    @Override
    public UpdateMaintenanceResponse update(UUID id, UpdateMaintenanceRequest request) {
        rules.checkIfMaintenanceExists(id);
        var maintenance = mapper.forRequest().map(request, Maintenance.class);
        maintenance.setId(id);
        repository.save(maintenance);
        return mapper.forResponse().map(maintenance, UpdateMaintenanceResponse.class);
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfMaintenanceExists(id);
        kafkaProducer.sendMessage(new MaintenanceDeletedEvent(repository.findById(id).orElseThrow().getCarId())
                , "maintenance-deleted");
        repository.deleteById(id);

    }
}
