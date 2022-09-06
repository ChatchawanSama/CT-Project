package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class ShopList {
    private final List<Shop> shops;

    public ShopList() {
        shops = new ArrayList<>();
    }

    public void add(Shop shop) {
        shops.add(shop);
    }

    public Shop searchShopFromName(String name) {
        for (Shop shop : shops) {
            if (shop.getName().equals(name))
                return shop;
        }
        return null;
    }

    public Shop searchShopFromProduct(Product target) {
        for (Shop shop : shops)
            for (Product product : shop.getProductList().getProducts())
                if (target.equals(product))
                    return shop;

        return null;
    }

    public ShopList getNotBannedShops() {
        ShopList temp = new ShopList();
        for (Shop shop : shops)
            if (!shop.getOwner().isBanned())
                temp.add(shop);
        return temp;
    }

    public ProductList getAllProductList() {
        ProductList productList = new ProductList();

        for (Shop shop : shops) {
            for (Product product : shop.getProductList().getProducts())
                productList.add(product);
        }
        return productList;
    }

    public int count() {
        return shops.size();
    }

    public String toCsv() {
        String result = "";

        for (Shop shop : shops)
            result += shop.toCsv() + "\n";

        return result;
    }
}
