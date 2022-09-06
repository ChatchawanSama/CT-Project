package ku.cs.models;

import java.time.LocalDateTime;

public class Review implements Reportable {
    private final User buyer;
    private final Product product;
    private int rating;
    private String content;
    private LocalDateTime time;

    public Review(Product product, User buyer, int rating, String content, LocalDateTime time) {
        this.product = product;
        this.buyer = buyer;
        this.rating = rating;
        this.content = content;
        this.time = time;
    }

    public User getBuyer() {
        return buyer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Product getProduct() {
        return product;
    }

    public String toCsv() {
        return product.getName() + ", " + buyer.getUsername() + ", " + rating + ", " + "\"" + content + "\"" + ", " + time;
    }

    @Override
    public String toReport() {
        return "Review, \"" + product.getName() + "\", \"" + buyer.getUsername() + "\", \"" + rating + "\", \"" + content + "\"";
    }
}
