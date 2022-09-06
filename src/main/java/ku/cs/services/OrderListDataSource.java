package ku.cs.services;

import ku.cs.models.Order;
import ku.cs.models.OrderList;
import ku.cs.models.Shop;
import ku.cs.models.User;

import java.io.*;
import java.time.LocalDateTime;

public class OrderListDataSource implements DataSource<OrderList> {
    private final OrderList orderList;
    private final String directory;
    private final String filename;

    public OrderListDataSource(String directory, String filename) {
        orderList = new OrderList();
        this.directory = directory;
        this.filename = filename;
        createFileIfNotExists();
    }

    public OrderListDataSource(Shop shop) {
        this(shop.getDirectory(), "orderList.csv");
    }

    private void createFileIfNotExists() {
        File file = new File(this.directory);

        if (!file.exists())
            file.mkdir();

        file = new File(this.directory + File.separator + this.filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public OrderList readData() {
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(this.directory + File.separator + this.filename);
            buffer = new BufferedReader(reader);

            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");

                String buyerName = data[0].trim();
                String productName = data[1].trim();
                int quantity = Integer.parseInt(data[2].trim());
                double price = Double.parseDouble(data[3].trim());
                String trackingNumber = data[4].trim();
                LocalDateTime time = LocalDateTime.parse(data[5].trim());


                User buyer = new UserListDataSource().readData().findUserByUsername(buyerName);
                orderList.add(new Order(buyer, productName, quantity, price, trackingNumber, time));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return orderList;
    }

    public void writeData(OrderList orderList) {
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(this.directory + File.separator + this.filename);
            buffer = new BufferedWriter(writer);

            buffer.write(orderList.toCsv());
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
