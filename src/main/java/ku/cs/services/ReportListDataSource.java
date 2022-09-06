package ku.cs.services;

import ku.cs.models.*;

import java.io.*;
import java.time.LocalDateTime;

public class ReportListDataSource implements DataSource<ReportList> {
    private final String directory;
    private final String filename;
    private final String path;
    private final ReportList reportList;
    private final AccountList accountList;
    private final AccountListDataSource accountDataSource;
    private final ShopList shopList;
    private final ShopListDataSource shopDataSource;
    private final ProductList productList;
    private final ProductListDataSource productDataSource;
    private ReviewList reviewList;
    private ReviewListDataSource reviewDataSource;

    public ReportListDataSource() {
        this("data", "report.csv");
    }

    public ReportListDataSource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
        path = directory + File.separator + filename;
        reportList = new ReportList();
        accountDataSource = new AccountListDataSource();
        accountList = accountDataSource.readData();
        shopDataSource = new ShopListDataSource();
        shopList = shopDataSource.readData();
        productDataSource = new ProductListDataSource(true);
        productList = productDataSource.readData();
        initialFileIfNotExist();
    }

    private void initialFileIfNotExist() {
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ReportList readData() {
        File file = new File(path);
        FileReader reader = null;
        BufferedReader buffer = null;
        try {
            reader = new FileReader(path);
            buffer = new BufferedReader(reader);
            String line = "";
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data[0].equals("Review")) {
                    Product product = productList.searchProductByName(data[1].trim().replaceAll("^\"|\"$", ""));
                    Shop shop = shopList.searchShopFromProduct(product);
                    reviewDataSource = new ReviewListDataSource(shop);
                    reviewList = reviewDataSource.readData();
                    Review targetReview = reviewList.searchReviewFromName(data[2].trim().replaceAll("^\"|\"$", ""));
                    Account account = accountList.searchByUsername(data[5].trim().replaceAll("^\"|\"$", ""));
                    String subject = data[6].trim().replaceAll("^\"|\"$", "");
                    String detail = data[7].trim().replaceAll("^\"|\"$", "");
                    LocalDateTime time = LocalDateTime.parse(data[8].trim().replaceAll("^\"|\"$", ""));
                    reportList.addReport(new Report(targetReview, account, subject, detail, time));
                }
                if (data[0].equals("Product")) {
                    Product targetProduct = productList.searchProductByName(data[1].trim().replaceAll("^\"|\"$", ""));
                    Account account = accountList.searchByUsername(data[5].trim().replaceAll("^\"|\"$", ""));
                    String subject = data[6].trim().replaceAll("^\"|\"$", "");
                    String detail = data[7].trim().replaceAll("^\"|\"$", "");
                    LocalDateTime time = LocalDateTime.parse(data[8].trim().replaceAll("^\"|\"$", ""));
                    reportList.addReport(new Report(targetProduct, account, subject, detail, time));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reportList;
    }

    public void writeData(ReportList reportList) {
        File file = new File(path);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(path);
            buffer = new BufferedWriter(writer);
            buffer.write(reportList.toCsv());
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