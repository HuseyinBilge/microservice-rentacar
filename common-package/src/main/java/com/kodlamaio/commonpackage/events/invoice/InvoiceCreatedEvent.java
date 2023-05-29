package com.kodlamaio.commonpackage.events.invoice;

import com.kodlamaio.commonpackage.events.Event;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreatedEvent implements Event {
    private String cardHolder;
    private LocalDateTime dateTime;
    private UUID carId;
    private String modelName;
    private String brandName;
    private String plate;
    private int modelYear;
    private double dailyPrice;
    private int rentedForDays;
    private double price;
}