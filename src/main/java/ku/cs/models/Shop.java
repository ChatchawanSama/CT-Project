package ku.cs.models;

import java.io.File;

public class Shop {
    private String name;
    private final User owner;
    private ProductList productList;
    private final OrderList orderList;
    private final String directory;
    private String shopImageProfilePath;

    public Shop(User owner, String name, ProductList productList, OrderList orderList, String pathShopImg) {
        this.name = name;
        this.owner = owner;
        this.productList = productList;
        this.orderList = orderList;
        this.directory = owner.getDirectory() + File.separator + "shop";
        shopImageProfilePath = pathShopImg;
        createDirectory();
    }

    public void addProduct(Product product) {
        this.productList.add(product);
    }

    public void removeProduct(Product product) {
        this.productList.remove(product);
    }

    private void createDirectory() {
        File directory = new File(getDirectory());
        if (!directory.exists())
            directory.mkdir();
    }

    public boolean sell(Order order) {
        if (productList.searchProductByName(order.getOrderName()).getQuantity() >= order.getQuantity()) {
            productList.searchProductByName(order.getOrderName()).setQuantity(productList.searchProductByName(order.getOrderName()).getQuantity() - order.getQuantity());
            orderList.add(order);
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductList getProductList() {
        return productList;
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }

    public OrderList getOrderList() {
        return orderList;
    }

    public User getOwner() {
        return owner;
    }

    public String getDirectory() {
        return directory;
    }

    public String getShopImageProfilePath() {
        return shopImageProfilePath;
    }

    public void setShopImageProfilePath(String shopImageProfilePath) {
        this.shopImageProfilePath = shopImageProfilePath;
    }

    public String toCsv() {
        return "\"" + owner.getUsername() + "\"" + ", " + "\"" + this.name + "\"" + ", " + "\"" + shopImageProfilePath + "\"";
    }

}
