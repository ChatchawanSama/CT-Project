package ku.cs.models;

import java.time.LocalDateTime;

// 1 product per order
public class Order {
    private final String orderName;
    private final User buyer;
    private final int quantity;
    private double price;
    private String trackingNumber;
    private final LocalDateTime time;

    public Order(User buyer, String productName, int quantity, double price, String trackingNumber, LocalDateTime time) {
        this.buyer = buyer;
        this.orderName = productName;
        this.quantity = quantity;
        this.price = price;
        this.trackingNumber = trackingNumber;
        this.time = time;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public double getTotalPriceWithDiscountValue(Double discount) {
        return (price * quantity) - (discount);
    }

    public double getTotalPriceWithDiscountPercent(Double discount) {
        return (price * quantity) - ((discount / 100.0) * price * quantity);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public User getBuyer() {
        return buyer;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public boolean isShipped() {
        return !(trackingNumber.equals("null"));
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public String getBuyerName() {
        return buyer.getUsername();
    }

    public String toCsv() {
        return buyer.getUsername() + ", " + this.orderName + ", " + quantity + ", " + price + ", " + trackingNumber + ", " + time;
    }

    @Override
    public String toString() {
        return "Buyer : " + buyer.getUsername() + ", " + this.orderName + " : " + quantity + " : " + (trackingNumber.equals("null") ? "No tracking number" : trackingNumber);
    }
}
