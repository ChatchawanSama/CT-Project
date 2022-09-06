package ku.cs.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserList {
    private final List<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public String toCsv() {
        String result = "";
        for (User user : this.users) {
            result += user.toCsv() + "\n";
        }
        return result;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getSortedByLoginTime() {
        List<User> temp = users;
        temp.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getLastLogin() == null || o2.getLastLogin() == null) {
                    return 0;
                }
                if (o1.getLastLogin().isBefore(o2.getLastLogin())) {
                    return 1;
                }
                if (o1.getLastLogin().isAfter(o2.getLastLogin())) {
                    return -1;
                }
                return 0;
            }
        });
        return temp;
    }

    public List<User> getUsers() {
        return users;
    }
}
