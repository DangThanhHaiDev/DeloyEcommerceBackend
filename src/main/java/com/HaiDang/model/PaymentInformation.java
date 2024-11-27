package com.HaiDang.model;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class PaymentInformation {
    String cardHolderName;
    String cardNumber;
    LocalDate expirationDate;
    String cvv;
}
