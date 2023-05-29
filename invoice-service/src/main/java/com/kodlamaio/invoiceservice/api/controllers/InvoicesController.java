package com.kodlamaio.invoiceservice.api.controllers;

import com.kodlamaio.invoiceservice.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceservice.business.dto.response.GetAllInvoicesResponse;
import com.kodlamaio.invoiceservice.business.dto.response.GetInvoiceResponse;
import com.kodlamaio.invoiceservice.entities.Invoice;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoicesController {

    private final InvoiceService service;
    @GetMapping
    public ResponseEntity<List<GetAllInvoicesResponse>> getAll() {
        return new ResponseEntity<>(service.getAll(),HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetInvoiceResponse> get(@PathVariable String id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
