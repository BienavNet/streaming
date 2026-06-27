package com.api.streaming.domain.models;

import java.time.LocalDate;

import com.api.streaming.domain.valueObjects.AccountId;
import com.api.streaming.domain.valueObjects.OrderId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private OrderId id;
    private AccountId account;
    private int profileAmount;
    private LocalDate buyDate;
    private LocalDate finishDate;
    private double paymentAmount;


    public Order(OrderId id, AccountId account, int profileAmount, LocalDate buyDate, LocalDate finishDate,
            double paymentAmount) {
        this.id = id;
        this.account = account;
        this.profileAmount = profileAmount;
        this.buyDate = buyDate;
        this.finishDate = finishDate;
        this.paymentAmount = paymentAmount;

        this.validate();
    }

    private void validate() {
        this.validateDates(this.buyDate, this.finishDate);
        this.validatePaymentAmount(this.paymentAmount);
        this.validateProfileAmount(this.profileAmount);
    }

    public void setBuyDate(LocalDate date) {
        this.validateDates(date, this.finishDate);
        this.buyDate = date;
    }

    public void setFinishDate(LocalDate date) {
        this.validateDates(this.buyDate, date);
        this.finishDate = date;
    }

    public void setPaymentAmount(double amount) {
        this.validatePaymentAmount(amount);
        this.paymentAmount = amount;
    }

    public void setProfileAmount(int profileAmount) {
        this.validateProfileAmount(profileAmount);
        this.profileAmount = profileAmount;
    }

    private void validatePaymentAmount(double amount){
        if (amount < 0) 
            throw new IllegalArgumentException("Payment amount cannot be less than 0.");
    }

    private void validateProfileAmount(int profile) {
        if (profile < 0)
            throw new IllegalArgumentException("Profile amount should be more than 0.");
    }

    private void validateDates(LocalDate buyingDates, LocalDate finishingDates) {
        if (this.buyDate == null || this.finishDate == null)
            throw new IllegalArgumentException("Dates cannot be blank.");

        if (this.buyDate.isAfter(this.finishDate))
            throw new IllegalArgumentException("Buying date cannot be greater than finish date membership.");

        if (this.finishDate.isBefore(this.buyDate))
            throw new IllegalArgumentException("Finishing date cannot be lower than buy date membership.");
    }
    
    
}
