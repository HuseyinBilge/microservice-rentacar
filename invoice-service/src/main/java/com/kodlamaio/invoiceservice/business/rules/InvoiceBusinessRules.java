package com.kodlamaio.invoiceservice.business.rules;

import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.invoiceservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceBusinessRules {
    private final InvoiceRepository repository;
    public void checkInvoiceExist(String id){
        if(repository.findById(id).isEmpty()){
            throw new BusinessException("Invoice not found.");
        }
    }
}