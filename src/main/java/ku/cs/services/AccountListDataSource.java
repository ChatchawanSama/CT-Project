package ku.cs.services;

import ku.cs.models.*;

import java.io.*;
import java.time.LocalDateTime;

public class AccountListDataSource {
    private final AccountList accountList;
    private final String directory;
    private final String filename;
    private final String path;

    public AccountListDataSource() {
        this("data", "account.csv");
    }

    public AccountListDataSource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
        path = this.directory + File.separator + this.filename;
        accountList = new AccountList();
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

    public AccountList readData() {
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
                    accountList.add(new User(name, password, displayName, imagePath, shopName, lastLogin, isBanned, loginAfterBan));
                }

                if (data[0].equals("Admin")) {
                    String name = data[1].trim().replaceAll("^\"|\"$", "");
                    String password = data[2].trim();
                    String displayName = data[3].trim().replaceAll("^\"|\"$", "");
                    String imagePath = data[4].trim().replaceAll("^\"|\"$", "");
                    accountList.add(new Admin(name, password, displayName, imagePath));
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
        return accountList;
    }

    public void writeData(AccountList accountList) {
        File file = new File(path);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {

            writer = new FileWriter(path);
            buffer = new BufferedWriter(writer);
            buffer.write(accountList.toCsv());
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

    public void writeData(AdminList adminList, UserList userList) {
        File file = new File(path);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(path);
            buffer = new BufferedWriter(writer);
            buffer.write(adminList.toCsv() + userList.toCsv());
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
