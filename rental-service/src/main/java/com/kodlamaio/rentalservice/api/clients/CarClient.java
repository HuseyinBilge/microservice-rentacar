package com.kodlamaio.rentalservice.api.clients;


import com.kodlamaio.commonpackage.utils.dto.CarInfoClientResponse;
import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "stock-srvc", fallback = CarClientFallback.class)
public interface CarClient {
    @Retry(name = "rental-inventory")
    @GetMapping(value = "/api/cars/check-car-available/{id}")
    ClientResponse checkIfCarAvailable(@PathVariable UUID carId);

    @GetMapping(value = "/api/cars/get-car-info/{id}")
    CarInfoClientResponse getCarInfo(@PathVariable UUID id);
}
