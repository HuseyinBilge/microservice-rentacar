package com.kodlamaio.paymentservice.repository;

import com.kodlamaio.paymentservice.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findByCardNumber(String cardNumber);

    boolean existsByCardNumberAndCardHolderAndCardExpirationMonthAndCardExpirationYearAndCardCvv(
            String cardNumber, String cardHolder, int expirationMonth, int expirationYear, String cardCvv);

}
