package ku.cs.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Category {
    //clothes
    private final String name;
    //color, size
    private final List<Property> properties;

    public Category(String name) {
        this.name = name;
        properties = new ArrayList<>();
    }

    public void addProperty(String name) {
        properties.add(new Property(name));
    }

    public void addProduct(Product product, String propertyName, String type) {
        for (Property property : properties) {
            if (property.getName().equals(propertyName))
                property.addProduct(product, type);
        }
    }

    public void addProduct(Product product, LinkedHashMap<String, String> propertyAndTypeMap) {
        for (String key : propertyAndTypeMap.keySet())
            this.addProduct(product, key, propertyAndTypeMap.get(key));
    }

    public void addProduct(Product product, String type) {
        for (Property property : properties)
            if (property.getTypes().contains(type))
                property.addProduct(product, type);
    }

    public void addProductList(ProductList productList) {
        for (Product product : productList)
            if (product.getCategoryName().equals(this.name))
                this.addProduct(product, product.getPropertiesAndTypeMap());
    }

    public void addType(String propertyName, String type) {
        for (Property property : properties)
            if (property.getName().equals(propertyName))
                property.addType(type);
    }

    public void clearProducts() {
        for (Property property : properties)
            property.clearProductLists();
    }


    public ProductList getProducts() {
        for (Property property : properties)
            return property.getProducts();
        return null;
    }

    public ProductList getProductByType(String propertyName, String type) {
        for (Property property : properties)
            if (property.getName().equals(propertyName))
                return property.getProductsByType(type);

        return null;
    }

    public String getName() {
        return name;
    }

    public List<String> getPropertyNames() {
        List<String> temp = new ArrayList<>();
        for (Property property : properties)
            temp.add(property.getName());

        return temp;
    }

    public List<String> getTypesOfProperty(String propertyName) {
        for (Property property : properties)
            if (property.getName().equals(propertyName))
                return property.getTypes();

        return new ArrayList<>();
    }

    public int getPropertyCount() {
        return properties.size();
    }

    public String toCsv() {
        String result = this.name + ", " + properties.size() + ", ";

        for (Property property : properties)
            result += property.toCsv();

        return result;
    }

    @Override
    public String toString() {
        String result = "[ " + name + " ]" + "\n";

        for (Property property : properties)
            result += property.toString();

        return result;

    }
}
