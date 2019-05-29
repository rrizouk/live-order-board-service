package com.silverbars.liveorderboard.dto;

import com.silverbars.liveorderboard.domain.Order;
import com.silverbars.liveorderboard.domain.OrderType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.silverbars.liveorderboard.domain.OrderType.BUY;
import static com.silverbars.liveorderboard.domain.OrderType.SELL;
import static com.silverbars.liveorderboard.service.OrderServiceTest.createOrder;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SummaryInformationTest {

    private SummaryInformation underTest;

    @Test
    public void should_get_buy_orders_summary_in_descending_price_order() {
        this.underTest = new SummaryInformation(createOrders(BUY));

        List<OrderSummary> result = underTest.getOrdersSummary();
        assertNotNull(result);
        assertEquals(3,result.size());

        assertEquals(1.2,result.get(0).getQuantity(),0.0 );
        assertEquals(Optional.of(310L).get(), result.get(0).getPrice());
        assertEquals(1.5,result.get(1).getQuantity(),0.0 );
        assertEquals(Optional.of(307L).get(), result.get(1).getPrice());

        assertEquals("1.2 kg for £310",result.get(0).toString());
        assertEquals("1.5 kg for £307",result.get(1).toString());
        assertEquals("5.5 kg for £306",result.get(2).toString());
    }


    @Test
    public void should_get_sell_orders_summary_in_ascending_price_order() {
        this.underTest = new SummaryInformation(createOrders(SELL));

        List<OrderSummary> result = underTest.getOrdersSummary();
        assertNotNull(result);
        assertEquals(3,result.size());

        assertEquals(5.5,result.get(0).getQuantity(),0.0 );
        assertEquals(Optional.of(306L).get(), result.get(0).getPrice());
        assertEquals(1.5,result.get(1).getQuantity(),0.0 );
        assertEquals(Optional.of(307L).get(), result.get(1).getPrice());

        assertEquals("5.5 kg for £306",result.get(0).toString());
        assertEquals("1.5 kg for £307",result.get(1).toString());
        assertEquals("1.2 kg for £310",result.get(2).toString());
    }


    private List<Order> createOrders(OrderType orderType) {
        List<Order> orders = new ArrayList<>();
        Order order1 = createOrder("user1", 3.5d, 306L, orderType);
        Order order2 = createOrder("user2",1.2d, 310L, orderType);
        Order order3 = createOrder("user3",1.5d, 307L, orderType);
        Order order4 = createOrder("user4",2.0d, 306L, orderType);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        return orders;
    }
}