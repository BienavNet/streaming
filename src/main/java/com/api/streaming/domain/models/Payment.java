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

        this.validate();
    }

    public void setNumber(String number) {
        this.validateNumber(number);
        this.number = number;
    }

    public void setMonth(Integer month) {
        this.validateMonth(month);
        this.month = month;
    }

    public void setCvv(String cvv) {
        this.validateCvv(cvv);
        this.cvv = cvv;
    }

    private void validate() {
        this.validateNumber(this.number);
        this.validateMonth(this.month);
        this.validateCvv(this.cvv);
    }

    private void validateCvv(String cvv) {
        if (this.isNumber(cvv)){
            if (cvv.length() == 3 && number.startsWith("4") || number.startsWith("5") || number.startsWith("6"))
                return;
            if (cvv.length() == 4 && number.startsWith("3"))
                return;
        }

        throw new IllegalArgumentException("Invalid cvv for the registered card payment.");
    }

    private void validateNumber(String number) {

        if (number != null && !number.isBlank()) {
            if (number.length() == 16 && number.startsWith("4") || number.startsWith("5"))
                return;
            if (number.startsWith("3") && number.length() == 15)
                return;
        }

        throw new IllegalArgumentException("Card number is invalid.");
    }

    private void validateMonth(Integer month) {
        if (month == null)
            throw new IllegalArgumentException("Month specified is invalid.");
        if (month <=0 || month > 12)
            throw new IllegalArgumentException("Month specified is invalid.");
    }

    private boolean isNumber(String value) {
        try {
            if (value != null && !value.isBlank()){
                Integer.parseInt(value);
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
