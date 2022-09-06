package ku.cs.models;

import java.util.*;

public class ProductList implements Iterable<Product> {
    private final List<Product> products;

    public ProductList() {
        products = new ArrayList<>();
    }

    public void add(Product product) {
        products.add(product);
    }

    public String toCsv() {
        String line = "";

        for (Product product : products)
            line += product.toCsv() + "\n";

        return line;
    }

    public void remove(Product product) {
        for (Product it : products)
            if (it.equals(product)) {
                products.remove(product);
                return;
            }
    }

    public Product searchProductByName(String name) {
        for (Product temp : products)
            if (temp.getName().equals(name))
                return temp;

        return null;
    }

    public void sort(Comparator<Product> comparator) {
        products.sort(comparator);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void sortByName() {
        this.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getName().toLowerCase(Locale.ROOT).compareTo(o2.getName().toLowerCase(Locale.ROOT));
            }
        });

    }

    public void sortByTime() {
        this.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if (o1.getTime().isBefore(o2.getTime()))
                    return 1;
                if (o1.getTime().isAfter(o2.getTime()))
                    return -1;
                return 0;
            }
        });
    }

    public void sortByPrice(boolean isReversed) {
        this.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if (isReversed)
                    return Double.compare(o2.getPrice(), o1.getPrice());
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });
    }

    public ProductList filterProductByPriceRange(double min, double max) {
        ProductList productList = new ProductList();

        for (Product product : products)
            if (product.getPrice() <= max && product.getPrice() >= min)
                productList.add(product);

        return productList;
    }

    public int count() {
        return products.size();
    }

    @Override
    public String toString() {
        String result = "";

        for (Product product : products)
            result += product.getName() + "\n";

        return result;
    }

    @Override
    public Iterator<Product> iterator() {
        return products.iterator();
    }
}