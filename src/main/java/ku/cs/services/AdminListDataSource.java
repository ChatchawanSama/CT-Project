package ku.cs.services;

import ku.cs.models.Admin;
import ku.cs.models.AdminList;

import java.io.*;

public class AdminListDataSource {
    private final AdminList adminList;
    private final String directory;
    private final String filename;
    private final String path;

    public AdminListDataSource() {
        this("data", "account.csv");
    }

    public AdminListDataSource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
        path = this.directory + File.separator + this.filename;
        adminList = new AdminList();
        initialFileIfNotExist();
    }

    public void initialFileIfNotExist() {
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

    //role , Username, Password, displayName, imagePath, shopName, lastLogin, isBanned, LoginAfterBan
    public AdminList readData() {
        File file = new File(path);
        FileReader reader = null;
        BufferedReader buffer = null;
        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            String line = "";
            while ((line = buffer.readLine()) != null) {

                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data[0].equals("Admin")) {
                    String name = data[1].trim().replaceAll("^\"|\"$", "");
                    String password = data[2].trim();
                    String displayName = data[3].trim().replaceAll("^\"|\"$", "");
                    String imagePath = data[4].trim().replaceAll("^\"|\"$", "");
                    adminList.addAdmin(new Admin(name, password, displayName, imagePath));
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
        return adminList;
    }
}
