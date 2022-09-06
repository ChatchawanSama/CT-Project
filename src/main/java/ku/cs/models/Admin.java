package ku.cs.models;

public class Admin extends Account {

    public Admin(String username, String password, String displayName, String profilePicturePath) {
        super(username, password, displayName, profilePicturePath);
    }

    public Admin(String username, String password, String displayName) {
        super(username, password, displayName);
    }

    public void login() {
    }

    public void ban(User target) {
        target.isBanned = true;
    }

    public void unban(User target) {
        target.isBanned = false;
    }

    @Override
    public String toCsv() {
        return "Admin, " + getUsername() + ", " +
                getPassword() + ", " +
                getDisplayName() + ", " +
                getProfilePicturePath();
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
