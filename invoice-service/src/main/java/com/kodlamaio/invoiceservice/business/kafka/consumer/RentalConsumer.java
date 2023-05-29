package com.kodlamaio.invoiceservice.business.kafka.consumer;

import com.kodlamaio.commonpackage.events.invoice.InvoiceCreatedEvent;
import com.kodlamaio.invoiceservice.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceservice.entities.Invoice;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalConsumer {
    private final InvoiceService service;
    private final ModelMapper mapper;

    @KafkaListener(
            topics = "rental-invoice-created",
            groupId = "rental-invoice-create")
    public void consume(InvoiceCreatedEvent event){
        service.add(mapper.map(event, Invoice.class));
    }

}