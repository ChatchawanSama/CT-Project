package ku.cs.models;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Product implements Reportable {
    // ข้อมูลสินค้า -> ชื่อสินค้า รายละเอียดสินค้า จำนวนสินค้าที่มี ราคาสินค้า จำนวนสินค้าที่จะแจ้งเตือน รีวิว
    private String name;
    private final String detail;
    private int quantity;
    private double price;
    private final String imagePath;
    private final int lowQuantityAlertAt;
    private final LocalDateTime time;
    private final String categoryName;
    private final LinkedHashMap<String, String> propertiesAndTypeMap;

    public Product(String name, String detail, double price, int quantity, int lowQuantityAlertAt, Category category, LinkedHashMap<String, String> propertiesAndTypeMap, String imagePath, LocalDateTime time) {
        this.name = name;
        this.detail = detail;
        this.quantity = quantity;
        this.price = price;
        this.lowQuantityAlertAt = lowQuantityAlertAt;
        this.imagePath = imagePath;
        this.time = time;
        this.categoryName = category.getName();
        this.propertiesAndTypeMap = propertiesAndTypeMap;

        category.addProduct(this, propertiesAndTypeMap);
    }

    public boolean isLowQuantity() {
        return quantity <= lowQuantityAlertAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getLowQuantityAlertAt() {
        return lowQuantityAlertAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocalDateTime getTime() {
        return time;
//                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public String getCategoryName() {
        return categoryName;
    }

    public LinkedHashMap<String, String> getPropertiesAndTypeMap() {
        return propertiesAndTypeMap;
    }

    public String propertyAndTypeToString() {
        String result = "";
        for (String key : propertiesAndTypeMap.keySet())
            result += key + " : " + propertiesAndTypeMap.get(key) + " , ";
        StringBuffer stringBuffer = new StringBuffer(result.trim());
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    @Override
    public String toString() {
        return name + " " + price;
    }

    public String toCsv() {
        String result = "\"" + name + "\"" + ", " + "\"" + detail + "\"" + ", " + price + ", " +
                quantity + ", " + lowQuantityAlertAt + ", " + categoryName + ", ";

        for (String key : propertiesAndTypeMap.keySet())
            result += key + ", " + propertiesAndTypeMap.get(key) + ", ";

        result += "\"" + imagePath + "\"" + ", " + time;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return quantity == product.quantity && Double.compare(product.price, price) == 0 && lowQuantityAlertAt == product.lowQuantityAlertAt && Objects.equals(name, product.name) && Objects.equals(detail, product.detail) && Objects.equals(imagePath, product.imagePath) && Objects.equals(time, product.time) && Objects.equals(categoryName, product.categoryName) && Objects.equals(propertiesAndTypeMap, product.propertiesAndTypeMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, detail, quantity, price, imagePath, lowQuantityAlertAt, time, categoryName, propertiesAndTypeMap);
    }

    @Override
    public String toReport() {
        return "Product, \"" + name + "\", \"" + detail + "\", \"" + price + "\", \"" + imagePath + "\"";
    }
}