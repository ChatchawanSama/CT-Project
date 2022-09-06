package ku.cs.services;

import ku.cs.models.*;

import java.io.*;

public class ShopListDataSource {
    private final ShopList shopList;
    private final String directory;
    private final String filename;
    private final String file;

    public ShopListDataSource() {
        this("data", "shopList.csv");
    }

    public ShopListDataSource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
        file = this.directory + File.separator + this.filename;
        shopList = new ShopList();
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        File file = new File(directory);
        if (!file.exists())
            file.mkdir();

        file = new File(this.file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ShopList readData() {
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            File file = new File(this.file);
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null) {
                //ownerName, shopName
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String ownerName = data[0].trim().replaceAll("^\"|\"$", "");
                String shopName = data[1].trim().replaceAll("^\"|\"$", "");
                String shopPathImg = data[2].trim().replaceAll("^\"|\"$", "");

                UserList userList = new UserListDataSource().readData();
                User shopOwner = userList.findUserByUsername(ownerName);

                String shopOwnerExistedDataDirectory = shopOwner.getDirectory() + File.separator + "shop";
                ProductList productList = new ProductListDataSource(shopOwnerExistedDataDirectory, "productList.csv").readData();
                OrderList orderList = new OrderListDataSource(shopOwnerExistedDataDirectory, "orderList.csv").readData();

                shopOwner.setUpShop(shopName, productList, orderList, shopPathImg);
                shopList.add(shopOwner.getShop());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot Open file in ShopListDataSource");
        } finally {
            try {
                reader.close();
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return shopList;
    }

    public void writeData(ShopList shopList) {
        File file = new File(this.file);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {

            writer = new FileWriter(this.file);
            buffer = new BufferedWriter(writer);
            buffer.write(shopList.toCsv());
            buffer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
