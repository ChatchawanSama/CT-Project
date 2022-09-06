package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class AdminList {
    private final List<Admin> admins;

    public AdminList() {
        admins = new ArrayList<>();
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    public String toCsv() {
        String result = "";
        for (Admin admin : this.admins) {
            result += admin.toCsv() + "\n";
        }
        return result;
    }

    public Admin findAdminByUsername(String username) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username)) {
                return admin;
            }
        }
        return null;
    }

    public List<Admin> getUser() {
        return admins;
    }
}
