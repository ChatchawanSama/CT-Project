package ku.cs.services;

import ku.cs.models.ProductList;
import ku.cs.models.PromotionList;
import ku.cs.models.PromotionPurchase;
import ku.cs.models.PromotionQuantity;

import java.io.*;

public class PromotionListDataSource implements DataSource<PromotionList> {

    private ProductList promotionList;
    private final String directoryName;
    private final String fileName;

    public PromotionListDataSource() {
        this("data", "allPromotion.csv");
        promotionList = new ProductList();
    }

    public PromotionListDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        File file = new File(directoryName);

        if (!file.exists())
            file.mkdir();

        file = new File(directoryName + File.separator + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public PromotionList readData() {
        PromotionList promotionList = new PromotionList();

        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);

            String line = "";

            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String typePromotion = data[0].trim();
                if (typePromotion.equals("PromotionPurchase")) {
                    promotionList.addPromotion(new PromotionPurchase(
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            Double.parseDouble(data[4].trim()),
                            data[5].trim().replaceAll("^\"|\"$", ""),
                            Double.parseDouble(data[6].trim())

                    ));
                } else if (typePromotion.equals("PromotionQuantity")) {
                    promotionList.addPromotion(new PromotionQuantity(
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            Double.parseDouble(data[4].trim()),
                            data[5].trim().replaceAll("^\"|\"$", ""),
                            Integer.parseInt(data[6].trim())

                    ));
                }
            }
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
        return promotionList;
    }

    @Override
    public void writeData(PromotionList promotionList) {
        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file);
            buffer = new BufferedWriter(writer);

            buffer.write(promotionList.toCsv());
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
