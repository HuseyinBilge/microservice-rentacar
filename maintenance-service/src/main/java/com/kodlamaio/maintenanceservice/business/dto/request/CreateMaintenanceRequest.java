package com.kodlamaio.maintenanceservice.business.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateMaintenanceRequest {
    private String information;
    private UUID carId;
}
