package com.silverbars.liveorderboard.dto;

import com.silverbars.liveorderboard.domain.Order;
import com.silverbars.liveorderboard.domain.OrderType;

import java.util.*;
import java.util.stream.Collectors;

import static com.silverbars.liveorderboard.domain.OrderType.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

public class SummaryInformation {

    private List<Order> orders;

    public SummaryInformation(List<Order> orders) {
        this.orders = orders;
    }

    public List<OrderSummary> getOrdersSummary() {
        List<OrderSummary> sellOrders = getOrdersSummaryByType(SELL);
        List<OrderSummary> buyOrders =  getOrdersSummaryByType(BUY);
        List<OrderSummary> ordersSummary = new ArrayList<>();
        ordersSummary.addAll(sellOrders);// assume sellers are shown first as the requirements are silent on this
        ordersSummary.addAll(buyOrders);
        return ordersSummary;
    }

    private List<OrderSummary> getOrdersSummaryByType(OrderType type) {
        List<OrderSummary> ordersSummary =  orders.stream()
                .filter(o -> type.equals(o.getType()))
                .collect(groupingBy(Order:: getPrice, summingDouble(o -> o.getQuantity())))
                .entrySet().stream()
                .map(entry -> new OrderSummary(entry.getKey(),entry.getValue()))
                .sorted(getSortingOrder(type))
                .collect(Collectors.toList());
        return ordersSummary;

    }

    private Comparator<? super OrderSummary> getSortingOrder(OrderType type) {
        if(SELL.equals(type)){
           return Comparator.comparingLong(OrderSummary:: getPrice);
        }
        return Comparator.comparingLong(OrderSummary:: getPrice).reversed();
    }



}
