package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class OrderList {
    private final List<Order> orders;

    public OrderList() {
        orders = new ArrayList<>();
    }

    public void add(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public OrderList getShippedOrders() {
        OrderList temp = new OrderList();

        for (Order order : orders)
            if (order.isShipped())
                temp.add(order);

        return temp;
    }

    public OrderList getNotShippedOrders() {
        OrderList temp = new OrderList();

        for (Order order : orders)
            if (!order.isShipped())
                temp.add(order);

        return temp;
    }

    public boolean contains(Product product, User buyer) {
        for (Order order : orders)
            if (order.getOrderName().equals(product.getName()))
                if (order.getTrackingNumber() != null && order.getBuyerName().equals(buyer.getUsername()) && !(order.getTrackingNumber().equals("null")))
                    return true;
        return false;
    }

    public void changeTrack(Order order, String newTrack) {
        for (Order order1 : orders) {
            if (order1 == order) {
                order1.setTrackingNumber(newTrack);
            }
        }
    }

    public String toCsv() {
        String result = "";
        for (Order order : orders)
            result += order.toCsv() + "\n";

        return result;
    }
}
