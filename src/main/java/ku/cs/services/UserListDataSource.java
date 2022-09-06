package ku.cs.services;

import ku.cs.models.User;
import ku.cs.models.UserList;

import java.io.*;
import java.time.LocalDateTime;

public class UserListDataSource {
    private final UserList userList;
    private final String directory;
    private final String filename;
    private final String path;

    public UserListDataSource() {
        this("data", "account.csv");
    }

    public UserListDataSource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
        path = this.directory + File.separator + this.filename;
        userList = new UserList();
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
    public UserList readData() {
        File file = new File(path);
        FileReader reader = null;
        BufferedReader buffer = null;
        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            String line = "";
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data[0].equals("User")) {
                    String name = data[1].trim().replaceAll("^\"|\"$", "");
                    String password = data[2].trim();
                    String displayName = data[3].trim().replaceAll("^\"|\"$", "");
                    String imagePath = data[4].trim().replaceAll("^\"|\"$", "");
                    String shopName = data[5].trim().replaceAll("^\"|\"$", "");
                    LocalDateTime lastLogin = null;
                    if (!data[6].trim().equals("null")) {
                        lastLogin = LocalDateTime.parse(data[6].trim());
                    }
                    boolean isBanned = Boolean.parseBoolean(data[7].trim());
                    int loginAfterBan = Integer.parseInt(data[8].trim());

                    userList.addUser(new User(name, password, displayName, imagePath, shopName, lastLogin, isBanned, loginAfterBan));
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
        return userList;
    }
}
