package ku.cs.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Property {
    //color
    private final String name;

    //black, {product1, product2}
    private final LinkedHashMap<String, ProductList> map;

    public Property(String name) {
        this.name = name;
        //iteration order does not change for LinkedHashMap
        map = new LinkedHashMap<>();
    }

    public void addType(String type) {
        map.put(type, null);
    }

    public void addProduct(Product product, String type) {
        ProductList productList = map.get(type);

        if (productList == null)
            productList = new ProductList();

        productList.add(product);
        map.put(type, productList);
    }

    public void clearProductLists() {
        for (String key : map.keySet())
            map.put(key, null);
    }

    public ProductList getProductsByType(String type) {
        return map.get(type);
    }

    public ProductList getProducts() {
        ProductList productList = new ProductList();

        for (String type : map.keySet()) {
//          somehow, map.get(type) contains random nulls, so we need to filter it
            if (map.get(type) != null) {
                for (Product product : map.get(type))
                    productList.add(product);
            }
        }
        return productList;
    }

    public List<String> getTypes() {
        return new ArrayList<>(map.keySet());
    }

    public String getName() {
        return name;
    }

    public int typeCount() {
        return this.map.size();
    }

    public String toCsv() {
        String result = this.name + ", " + this.typeCount() + ", ";

        for (String type : map.keySet())
            result += type + ", ";

        return result;
    }

    @Override
    public String toString() {
        String result = name + ":" + "\n";
        for (String key : map.keySet())
            result += key + "-->" + getProductsByType(key) + (getProductsByType(key) == null ? "\n" : "");
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equals(name, property.name) && Objects.equals(map, property.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, map);
    }
}
