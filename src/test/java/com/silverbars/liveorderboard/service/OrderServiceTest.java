package com.silverbars.liveorderboard.service;

import com.silverbars.liveorderboard.domain.Order;
import com.silverbars.liveorderboard.domain.OrderType;
import com.silverbars.liveorderboard.dto.SummaryInformation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.silverbars.liveorderboard.domain.OrderType.BUY;
import static com.silverbars.liveorderboard.domain.OrderType.SELL;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class OrderServiceTest {

    private OrderService underTest;

    @Before
    public void setUp(){
        this.underTest = new OrderService();
    }

    @After
    public void tearDown()  {
        this.underTest = null;
    }

    @Test
    public void should_register_order() {
        String userId = "user1";
        double quantity = 3.5d;
        Long price = 306L;
        Order order = createOrder(userId,quantity,price,SELL);

        order = underTest.register(order);
        assertNotNull(order);
        assertNotNull(order.getId());

        List<Order> orders = underTest.getOrders();

        assertNotNull(orders);
        assertEquals(1,orders.size());
        assertEquals(userId,orders.get(0).getUserId());
        assertEquals(quantity,orders.get(0).getQuantity(),0.0);
        assertEquals(price,orders.get(0).getPrice());
        assertEquals(SELL,orders.get(0).getType());
    }

    @Test
    public void should_cancel_order() {
        String userId = "user";
        double quantity = 4.6d;
        Long price = 308L;
        Order order = createOrder(userId,quantity,price,BUY);
        Order order2 = createOrder(userId,quantity,price, SELL);

        order = underTest.register(order);
        assertNotNull(order);
        assertNotNull(order.getId());

        order2 = underTest.register(order2);
        assertNotNull(order2);
        assertNotNull(order2.getId());

        underTest.cancel(order2);

        List<Order> orders = underTest.getOrders();
        assertNotNull(orders);
        assertEquals(1,orders.size());
        assertEquals(userId,orders.get(0).getUserId());
        assertEquals(quantity,orders.get(0).getQuantity(),0.0);
        assertEquals(price,orders.get(0).getPrice());
        assertEquals(BUY,orders.get(0).getType());

    }

    @Test(expected = OrderNotFoundException.class)
    public void should_throw_exception_when_cancelling_non_existing_order() {
        String userId = "user";
        double quantity = 4.6d;
        Long price = 308L;
        Order order = createOrder(userId,quantity,price,BUY);
        Order order2 = createOrder(userId,quantity,price, SELL);

        order = underTest.register(order);
        assertNotNull(order);
        assertNotNull(order.getId());

        underTest.cancel(order2);
    }

    @Test
    public void should_merge_prices_and_show_sell_orders_in_ascending_order_for_summary_information() {

        Order order1 = createOrder("user1",3.5d, 306L,SELL);
        Order order2 = createOrder("user2",1.2d, 310L,SELL);
        Order order3 = createOrder("user3",1.5d, 307L,SELL);
        Order order4 = createOrder("user4",2.0d, 306L,SELL);

        underTest.register(order1);// this can be added as a list but kept this repeated code here for clarity
        underTest.register(order2);
        underTest.register(order3);
        underTest.register(order4);

       SummaryInformation summaryInformation = underTest.getSummaryInformation();
       assertNotNull(summaryInformation);
       assertNotNull(summaryInformation.getOrdersSummary());
       assertEquals(3,summaryInformation.getOrdersSummary().size());
       assertEquals(5.5,summaryInformation.getOrdersSummary().get(0).getQuantity(),0.0 );
       assertEquals(Optional.of(306L).get(), summaryInformation.getOrdersSummary().get(0).getPrice());

       assertEquals("5.5 kg for £306",summaryInformation.getOrdersSummary().get(0).toString());
       assertEquals("1.5 kg for £307",summaryInformation.getOrdersSummary().get(1).toString());
       assertEquals("1.2 kg for £310",summaryInformation.getOrdersSummary().get(2).toString());
    }

    @Test
    public void should_merge_prices_and_show_buy_orders_in_descending_order_for_summary_information() {

        Order order1 = createOrder("user1",3.5d, 306L,BUY);
        Order order2 = createOrder("user2",1.2d, 310L,BUY);
        Order order3 = createOrder("user3",1.5d, 307L,BUY);
        Order order4 = createOrder("user4",2.0d, 306L,BUY);

        underTest.register(order1);
        underTest.register(order2);
        underTest.register(order3);
        underTest.register(order4);

        SummaryInformation summaryInformation = underTest.getSummaryInformation();
        assertNotNull(summaryInformation);
        assertNotNull(summaryInformation.getOrdersSummary());
        assertEquals(3,summaryInformation.getOrdersSummary().size());
        assertEquals(1.2,summaryInformation.getOrdersSummary().get(0).getQuantity(),0.0 );
        assertEquals(Optional.of(310L).get(), summaryInformation.getOrdersSummary().get(0).getPrice());
        // etc...
        assertEquals("1.2 kg for £310",summaryInformation.getOrdersSummary().get(0).toString());
        assertEquals("1.5 kg for £307",summaryInformation.getOrdersSummary().get(1).toString());
        assertEquals("5.5 kg for £306",summaryInformation.getOrdersSummary().get(2).toString());
    }

    @Test
    public void should_merge_prices_and_show_buy_and_sell_orders_in_correct_order_for_summary_information() {
        Order order1 = createOrder("user1",3.5d, 306L,SELL);
        Order order2 = createOrder("user2",1.2d, 310L,SELL);
        Order order3 = createOrder("user3",1.5d, 307L,SELL);
        Order order4 = createOrder("user4",2.0d, 306L,SELL);
        Order order5 = createOrder("user5",10.0d, 306L,SELL);// order1 + order4 + order5 = 15.5

        Order order6 = createOrder("user6",3.5d, 306L,BUY);
        Order order7 = createOrder("user7",1.2d, 310L,BUY);
        Order order8 = createOrder("user8",1.5d, 307L,BUY);
        Order order9 = createOrder("user9",2.0d, 306L,BUY);// order6 + order9 = 5.5

        double totalSellQuantity = 15.5;
        double totalBuyQuantity = 5.5;

        underTest.register(order1);
        underTest.register(order2);
        underTest.register(order3);
        underTest.register(order4);
        underTest.register(order5);
        underTest.register(order6);
        underTest.register(order7);
        underTest.register(order8);
        underTest.register(order9);

        SummaryInformation summaryInformation = underTest.getSummaryInformation();
        assertNotNull(summaryInformation);
        assertNotNull(summaryInformation.getOrdersSummary());
        assertEquals(6,summaryInformation.getOrdersSummary().size());// 6 entries in total, 3 for each type


        assertEquals(totalSellQuantity,summaryInformation.getOrdersSummary().get(0).getQuantity(),0.0 );
        assertEquals(Optional.of(306L).get(), summaryInformation.getOrdersSummary().get(0).getPrice());
        assertEquals(totalBuyQuantity,summaryInformation.getOrdersSummary().get(5).getQuantity(),0.0 );
        assertEquals(Optional.of(306L).get(), summaryInformation.getOrdersSummary().get(5).getPrice());

        assertEquals("15.5 kg for £306",summaryInformation.getOrdersSummary().get(0).toString());
        assertEquals("1.5 kg for £307",summaryInformation.getOrdersSummary().get(1).toString());
        assertEquals("1.2 kg for £310",summaryInformation.getOrdersSummary().get(2).toString());

        assertEquals("1.2 kg for £310",summaryInformation.getOrdersSummary().get(3).toString());
        assertEquals("1.5 kg for £307",summaryInformation.getOrdersSummary().get(4).toString());
        assertEquals("5.5 kg for £306",summaryInformation.getOrdersSummary().get(5).toString());
    }

    // make this visible for the other test
    public static Order createOrder(String userId, double quantity, Long price, OrderType type) {
        return new Order(userId,quantity,price,type);
    }
}
