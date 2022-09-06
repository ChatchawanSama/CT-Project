package ku.cs.services;

import ku.cs.models.Category;
import ku.cs.models.Product;
import ku.cs.models.ProductList;
import ku.cs.models.Shop;

import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class ProductListDataSource implements DataSource<ProductList> {
    private final ProductList productList;
    private final String directory;
    private final String filename;

    public ProductListDataSource(String directory, String filename) {
        productList = new ProductList();
        this.directory = directory;
        this.filename = filename;
        createFileIfNotExists();
    }

    public ProductListDataSource(Shop shop) {
        this(shop.getDirectory(), "productList.csv");
    }

    public ProductListDataSource(boolean includeBannedShop) {
        this("data", "allProductList.csv");
        this.writeData(readAllProductList(includeBannedShop));
    }

    private void createFileIfNotExists() {
        File file = new File(this.directory);

        if (!file.exists())
            file.mkdir();

        file = new File(this.directory + File.separator + filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ProductList readData() {

        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(directory + File.separator + filename);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null) {

                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                System.out.println(Arrays.toString(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")));

                String name = data[0].trim().replaceAll("^\"|\"$", "");
                String detail = data[1].trim().replaceAll("^\"|\"$", "");
                double price = Double.parseDouble(data[2].trim());
                int quantity = Integer.parseInt(data[3].trim());
                int lowQuantityAlertAt = Integer.parseInt(data[4].trim());
                String categoryName = data[5].trim();
                Category category = new CategoryListDataSource().readData().searchByName(categoryName);
                LinkedHashMap<String, String> propertyAndTypeMap = new LinkedHashMap<>();

                int i = 6;
                for (int counter = 0; counter < category.getPropertyCount(); counter++) {
//                    System.out.println("Index : " + i + " --> " + data[i].trim());
//                    System.out.println("Index : " + (i+1) + " --> " + data[i+1].trim());
                    propertyAndTypeMap.put(data[i].trim(), data[i + 1].trim());

                    i += 2;
                }

                String imagePath = data[i].trim().replaceAll("^\"|\"$", "");
                i++;
//                System.out.println("Index : " + i + " --> " + data[i].trim());
                LocalDateTime time = LocalDateTime.parse(data[i].trim());

//                productList.add(new Product(name, detail, price, quantity, lowQuantityAlertAt, imagePath, time));
                productList.add(new Product(name, detail, price, quantity, lowQuantityAlertAt, category, propertyAndTypeMap, imagePath, time));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot Open file in ProductListDataSource");
        } finally {
            try {
                reader.close();
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return productList;
    }

    public ProductList readAllProductList(boolean includeBannedShop) {
        if(includeBannedShop)
            return new ShopListDataSource().readData().getAllProductList();

        return new ShopListDataSource().readData().getNotBannedShops().getAllProductList();
    }

    public void writeData(ProductList productList) {
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(directory + File.separator + filename);
            buffer = new BufferedWriter(writer);

            buffer.write(productList.toCsv());
            buffer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
