package com.api.streaming.domain.models;

import com.api.streaming.domain.valueObjects.PaymentId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {
    private PaymentId id;
    private String number;
    private Integer month;
    private Integer year;
    private String cvv;
    private String bankName;



    public Payment(PaymentId id, String number, Integer month, Integer year, String cvv, String bankName) {
        this.id = id;
        this.number = number;
        this.month = month;
        this.year = year;
        this.cvv = cvv;
        this.bankName = bankName;

        validate();
    }

    private void validate() {
        if (this.number.startsWith("4") || this.number.startsWith("5") && this.number.length() != 16)
            throw new IllegalArgumentException("Card Number is invalid.");

        if (this.month <=0 || this.month > 12)
            throw new IllegalArgumentException("Month specified is invalid.");
        
        if (!isNumber(this.getCvv()))
            throw new IllegalArgumentException("Cvv is not a valid number.");

        if (this.number.startsWith("3") && this.cvv.length()!=4)
            throw new IllegalArgumentException("Cvv number is invalid for this card.");

        if (this.number.startsWith("4") || this.number.startsWith("5") && this.cvv.length()!=3)
            throw new IllegalArgumentException("Cvv number is invalid for this card.");

    }

    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
