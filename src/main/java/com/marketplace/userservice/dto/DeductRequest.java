package com.marketplace.userservice.dto;

public class DeductRequest {
    private Long customerId;

    public Double getAmount() {
        return amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private Double amount;

}