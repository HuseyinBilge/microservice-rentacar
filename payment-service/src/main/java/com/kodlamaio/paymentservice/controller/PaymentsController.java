package com.kodlamaio.paymentservice.controller;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.paymentservice.business.abstracts.PaymentService;
import com.kodlamaio.paymentservice.business.dto.request.CreatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.request.UpdatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.response.CreatePaymentResponse;
import com.kodlamaio.paymentservice.business.dto.response.GetAllPaymentsResponse;
import com.kodlamaio.paymentservice.business.dto.response.GetPaymentResponse;
import com.kodlamaio.paymentservice.business.dto.response.UpdatePaymentResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payments")
public class PaymentsController {
    private final PaymentService service;

    @GetMapping
    public List<GetAllPaymentsResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetPaymentResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public CreatePaymentResponse add(@RequestBody @Valid CreatePaymentRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public UpdatePaymentResponse update(@PathVariable UUID id, @Valid @RequestBody UpdatePaymentRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PutMapping("/processRentalPayment")
    public ClientResponse processRentalPayment(@RequestBody @Valid CreateRentalPaymentRequest request) {
        return service.processRentalPayment(request);
    }

}