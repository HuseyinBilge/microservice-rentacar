package com.kodlamaio.rentalservice.business.concretes;

import com.kodlamaio.commonpackage.events.invoice.InvoiceCreatedEvent;
import com.kodlamaio.commonpackage.events.rental.RentalCreatedEvent;
import com.kodlamaio.commonpackage.events.rental.RentalDeletedEvent;

import com.kodlamaio.commonpackage.producer.KafkaProducer;
import com.kodlamaio.commonpackage.utils.dto.CarInfoClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.rentalservice.api.clients.CarClient;
import com.kodlamaio.rentalservice.business.abstracts.RentalService;
import com.kodlamaio.rentalservice.business.dto.requests.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.requests.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.responses.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.UpdateRentalResponse;
import com.kodlamaio.rentalservice.business.rules.RentalBusinessRules;
import com.kodlamaio.rentalservice.entities.Rental;
import com.kodlamaio.rentalservice.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RentalManager implements RentalService {
    private final RentalRepository repository;
    private final ModelMapperService mapper;
    private final RentalBusinessRules rules;
    private final KafkaProducer producer;
    private final CarClient carClient;

    @Override
    public List<GetAllRentalsResponse> getAll() {
        var rentals = repository.findAll();
        var response = rentals
                .stream()
                .map(rental -> mapper.forResponse().map(rental, GetAllRentalsResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetRentalResponse getById(UUID id) {
        rules.checkIfRentalExists(id);
        var rental = repository.findById(id).orElseThrow();
        var response = mapper.forResponse().map(rental, GetRentalResponse.class);

        return response;
    }

    @Override
    public CreateRentalResponse add(CreateRentalRequest request) {
        rules.ensureCarIsAvailable(request.getCarId());
        var rental = mapper.forRequest().map(request, Rental.class);
        rental.setId(null);
        rental.setTotalPrice(getTotalPrice(rental));

        //Payment
        RentalPayment(request, rental);

        //Invoice Creating
        createInvoiceAndSend(request, rental);

        rental.setRentedAt(LocalDate.now());
        repository.save(rental);
        sendKafkaRentalCreatedEvent(request.getCarId());
        var response = mapper.forResponse().map(rental, CreateRentalResponse.class);

        return response;
    }

    private void createInvoiceAndSend(CreateRentalRequest request, Rental rental) {
        InvoiceCreatedEvent invoiceCreatedEvent = new InvoiceCreatedEvent();
        CarInfoClientResponse carInfoClientResponse = carClient.getCarInfo(request.getCarId());

        invoiceCreatedEvent.setCardHolder(request.getPaymentRequest().getCardHolder());
        invoiceCreatedEvent.setDateTime(LocalDateTime.now());
        invoiceCreatedEvent.setCarId(request.getCarId());
        invoiceCreatedEvent.setModelName(carInfoClientResponse.getModelName());
        invoiceCreatedEvent.setBrandName(carInfoClientResponse.getBrandName());
        invoiceCreatedEvent.setPlate(carInfoClientResponse.getPlate());
        invoiceCreatedEvent.setModelYear(carInfoClientResponse.getModelYear());
        invoiceCreatedEvent.setDailyPrice(request.getDailyPrice());
        invoiceCreatedEvent.setRentedForDays(request.getRentedForDays());
        invoiceCreatedEvent.setPrice(getTotalPrice(rental));

        sendKafkaRentalInvoiceCreatedEvent(invoiceCreatedEvent);
    }

    private void RentalPayment(CreateRentalRequest request, Rental rental) {
        CreateRentalPaymentRequest createRentalPaymentRequest = new CreateRentalPaymentRequest(
         request.getPaymentRequest().getCardNumber(), request.getPaymentRequest().getCardHolder(),
                request.getPaymentRequest().getCardExpirationYear(),
                request.getPaymentRequest().getCardExpirationMonth(),
                request.getPaymentRequest().getCardCvv(), rental.getTotalPrice()
        );
        rules.checkPaymentIsDone(createRentalPaymentRequest);
    }

    @Override
    public UpdateRentalResponse update(UUID id, UpdateRentalRequest request) {
        rules.checkIfRentalExists(id);
        var rental = mapper.forRequest().map(request, Rental.class);
        rental.setId(id);
        repository.save(rental);
        var response = mapper.forResponse().map(rental, UpdateRentalResponse.class);

        return response;
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfRentalExists(id);
        sendKafkaRentalDeletedEvent(id);
        repository.deleteById(id);
    }

    private double getTotalPrice(Rental rental) {
        return rental.getDailyPrice() * rental.getRentedForDays();
    }

    private void sendKafkaRentalCreatedEvent(UUID carId) {
        producer.sendMessage(new RentalCreatedEvent(carId), "rental-created");
    }

    private void sendKafkaRentalDeletedEvent(UUID id) {
        var carId = repository.findById(id).orElseThrow().getCarId();
        producer.sendMessage(new RentalDeletedEvent(carId), "rental-deleted");
    }
    private void sendKafkaRentalInvoiceCreatedEvent(InvoiceCreatedEvent event) {
        producer.sendMessage(event, "rental-invoice-created");
    }
}