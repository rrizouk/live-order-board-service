package com.silverbars.liveorderboard.dto;

public class OrderSummary {

    private final Long price;
    private final Double quantity;

    public Long getPrice() {
        return price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public OrderSummary(Long price, Double quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    // added so that the tests are easy to read
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(quantity.doubleValue());
        builder.append(" kg for £");
        builder.append(price);
        return builder.toString();
    }
}
