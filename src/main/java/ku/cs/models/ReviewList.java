package ku.cs.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ReviewList implements Iterable<Review> {
    private final List<Review> reviews;

    public ReviewList() {
        reviews = new ArrayList<>();
    }

    public void add(Review review) {
        reviews.add(review);
    }

    public void remove(Review review) {
        reviews.remove(review);
    }

    public double getAverageRating() {
        double sum = 0;
        for (Review review : reviews)
            sum += review.getRating();

        if (reviews.size() != 0)
            return sum / reviews.size();
        return -1;
    }

    public Review searchReviewFromName(String username) {
        for (Review review : reviews) {
            if (review.getBuyer().getUsername().equals(username)) {
                return review;
            }
        }
        return null;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public String toCsv() {
        String result = "";
        for (Review review : reviews)
            result += review.toCsv() + "\n";

        return result;
    }

    public ReviewList getReviewListByProduct(Product product) {
        ReviewList reviewList = new ReviewList();
        for (Review review : reviews)
            if (review.getProduct().equals(product) && !review.getBuyer().isBanned)
                reviewList.add(review);

        return reviewList;
    }

    public void sort(Comparator<Review> comparator) {
        reviews.sort(comparator);
    }

    public void sortByOldest() {
        sort(new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                if (o1.getTime().isBefore(o2.getTime()))
                    return -1;
                if (o1.getTime().isAfter(o2.getTime()))
                    return 1;
                return 0;
            }
        });
    }

    public void sortByLatest() {
        sort(new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                if (o1.getTime().isBefore(o2.getTime()))
                    return 1;
                if (o1.getTime().isAfter(o2.getTime()))
                    return -1;
                return 0;
            }
        });
    }

    public void sortByRating(int rating) {
        reviews.removeIf(review -> review.getRating() != rating);
    }

    @Override
    public Iterator<Review> iterator() {
        return reviews.iterator();
    }
}
