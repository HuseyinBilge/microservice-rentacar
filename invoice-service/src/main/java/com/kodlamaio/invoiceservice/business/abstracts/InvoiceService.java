package com.kodlamaio.invoiceservice.business.abstracts;

import com.kodlamaio.invoiceservice.business.dto.response.GetAllInvoicesResponse;
import com.kodlamaio.invoiceservice.business.dto.response.GetInvoiceResponse;
import com.kodlamaio.invoiceservice.entities.Invoice;

import java.util.List;

public interface InvoiceService {
    void add(Invoice invoice);
    GetInvoiceResponse get(String id);
    List<GetAllInvoicesResponse> getAll();
    void delete(String id);
}