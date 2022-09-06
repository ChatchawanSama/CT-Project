package ku.cs.services;

import ku.cs.models.Category;
import ku.cs.models.CategoryList;

import java.io.*;

public class CategoryListDataSource implements DataSource<CategoryList> {

    private final CategoryList categoryList;
    private final String directory;
    private final String filename;

    public CategoryListDataSource() {
        this("data", "CategoryList.csv");
    }

    public CategoryListDataSource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
        categoryList = new CategoryList();
        initialFileIfNotExists();
    }

    private void initialFileIfNotExists() {
        File file = new File(directory);
        if (!file.exists())
            file.mkdir();

        file = new File(directory + File.separator + filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CategoryList readData() {
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(directory + File.separator + filename);
            buffer = new BufferedReader(reader);

            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
//                System.out.println(Arrays.toString(line.split(",")));

                String name = data[0].trim();
                int propertyCount = Integer.parseInt(data[1].trim());

                Category category = new Category(name);

                for (int i = 2, propertyCounter = 0; propertyCounter < propertyCount; i++, propertyCounter++) {

                    String property = data[i].trim();
                    category.addProperty(property);
//                    System.out.println("Property : " + property);

                    i++;
                    int typeCount = Integer.parseInt(data[i].trim());

                    for (int typeCounter = 0; typeCounter < typeCount; typeCounter++) {
                        i++;
//                        System.out.println("Index : " + i + " --> " + data[i].trim());
                        String type = data[i].trim();
                        category.addType(property, type);
                    }
                }

                categoryList.add(category);
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
        return categoryList;
    }

    public void writeData(CategoryList categoryList) {
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(directory + File.separator + filename);
            buffer = new BufferedWriter(writer);

            buffer.write(categoryList.toCsv());
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
