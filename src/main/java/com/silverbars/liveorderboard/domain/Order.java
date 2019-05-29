package com.silverbars.liveorderboard.domain;

public class Order {

    private final String userId;
    private final double quantity; // unit assumed to be kilograms
    private final Long price;// this is to keep things simple, in reality it should be a currency object containing currency code with BigDecimal value
    private final OrderType type;
    private Long id;

    public Order(String userId, double quantity, Long price, OrderType type) {
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public OrderType getType() {
        return type;
    }


    public double getQuantity() {
        return quantity;
    }

    public Long getPrice() {
        return price;
    }

    public String getUserId() {
        return userId;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
