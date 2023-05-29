package com.kodlamaio.maintenanceservice.business.dto.responce;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetMaintenanceResponse {
    private UUID id;
    private String information;
    private boolean isCompleted;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID carId;
}
