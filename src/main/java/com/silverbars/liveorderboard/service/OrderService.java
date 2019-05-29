package com.silverbars.liveorderboard.service;

import com.silverbars.liveorderboard.domain.Order;

import com.silverbars.liveorderboard.dto.SummaryInformation;

import java.util.*;

public class OrderService {

    private Map<Long,Order> orders = new HashMap<>();// this is to simulate a data store

    // this should be a void method, we are returning an order is for ease of testing, in the real world a get method will check for the existence of an order using the ID returned
    public Order register(Order order) {
      Long id = this.generateId();
      order.setId(id);
      this.orders.put(id,order);
      return order;
    }

    public void cancel(Order order) {
        if(!orders.containsKey(order.getId())){// admittedly the requirements are silent on this, but this is a nice to have so as not to confuse users by silently ignoring non existing orders
            throw new OrderNotFoundException("odrer not found:" + order.getId());
        }
        this.orders.remove(order.getId());
    }

    public SummaryInformation getSummaryInformation() {
        return new SummaryInformation(getOrders());
    }

    // this is simulate and auto-generated ID by a data source e.g. DB
    private Long generateId() {
        return new Random().nextLong();
    }

    // convenience method visible for tests
    List<Order> getOrders() {
        return new ArrayList<>(orders.values());
    }
}
