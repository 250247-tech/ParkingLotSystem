package com.parkinglot.models;

import com.parkinglot.enums.PaymentStatus;
import com.parkinglot.enums.PaymentType;
import java.time.LocalDateTime;

public class Payment {
    private int id;
    private int ticketId;
    private double amount;
    private PaymentType paymentType;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private String cardName;
    private double cashTendered;

    public Payment() {}

    public Payment(int ticketId, double amount, PaymentType paymentType) {
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public int getTicketId()                    { return ticketId; }
    public void setTicketId(int t)              { this.ticketId = t; }
    public double getAmount()                   { return amount; }
    public void setAmount(double a)             { this.amount = a; }
    public PaymentType getPaymentType()         { return paymentType; }
    public void setPaymentType(PaymentType t)   { this.paymentType = t; }
    public PaymentStatus getStatus()            { return status; }
    public void setStatus(PaymentStatus s)      { this.status = s; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
    public void setCreatedAt(LocalDateTime t)   { this.createdAt = t; }
    public String getCardName()                 { return cardName; }
    public void setCardName(String c)           { this.cardName = c; }
    public double getCashTendered()             { return cashTendered; }
    public void setCashTendered(double c)       { this.cashTendered = c; }

    public boolean initiateTransaction() {
        this.status = PaymentStatus.COMPLETED;
        return true;
    }
}
