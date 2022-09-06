package ku.cs.services;

import ku.cs.models.*;

import java.io.*;
import java.time.LocalDateTime;

public class ReviewListDataSource {
    private final ReviewList reviewList;
    private final String directory;
    private final String filename;

    public ReviewListDataSource(String directory, String filename) {
        reviewList = new ReviewList();
        this.directory = directory;
        this.filename = filename;
        createFileIfNotExists();
    }

    public ReviewListDataSource(Shop shop) {
        this(shop.getDirectory(), "reviewList.csv");
    }

    private void createFileIfNotExists() {
        File file = new File(directory);
        if (!file.exists())
            file.mkdir();


        String path = directory + File.separator + filename;
        file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ReviewList readData() {

        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            String path = directory + File.separator + filename;
            File file = new File(path);
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null) {

                //productName, buyerName, score, content, time

                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String productName = data[0].trim();
                String buyerName = data[1].trim();
                int score = Integer.parseInt(data[2].trim());
                String content = data[3].trim().replaceAll("^\"|\"$", "");
                LocalDateTime time = LocalDateTime.parse(data[4].trim());

                User buyer = new UserListDataSource().readData().findUserByUsername(buyerName);
                Product product = new ProductListDataSource(directory, "productList.csv").readData().searchProductByName(productName);
                reviewList.add(new Review(product, buyer, score, content, time));

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot Open file in ReviewListDataSource");
        } finally {
            try {
                reader.close();
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reviewList;
    }

    public void writeData(ReviewList reviewList) {

        FileWriter writer = null;
        BufferedWriter buffer = null;

        String path = directory + File.separator + filename;

        try {
            writer = new FileWriter(path);
            buffer = new BufferedWriter(writer);

            buffer.write(reviewList.toCsv());

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
