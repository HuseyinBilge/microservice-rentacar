package com.kodlamaio.invoiceservice.business.concretes;

import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.invoiceservice.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceservice.business.dto.response.GetAllInvoicesResponse;
import com.kodlamaio.invoiceservice.business.dto.response.GetInvoiceResponse;
import com.kodlamaio.invoiceservice.business.rules.InvoiceBusinessRules;
import com.kodlamaio.invoiceservice.entities.Invoice;
import com.kodlamaio.invoiceservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceManager implements InvoiceService {
    private final ModelMapperService mapper;
    private final InvoiceRepository repository;
    private final InvoiceBusinessRules rules;
    @Override
    public void add(Invoice invoice) {
        invoice.setId(UUID.randomUUID().toString());
        repository.save(invoice);
    }

    @Override
    public GetInvoiceResponse get(String id) {
        rules.checkInvoiceExist(id);
        return mapper.forResponse().map(repository.findById(id).orElseThrow(), GetInvoiceResponse.class);
    }

    @Override
    public List<GetAllInvoicesResponse> getAll() {
        return repository.findAll().stream()
                .map(invoice -> mapper.forResponse().map(invoice, GetAllInvoicesResponse.class))
                .toList();
    }

    @Override
    public void delete(String id) {
        rules.checkInvoiceExist(id);
        repository.deleteById(id);
    }
}
