package ku.cs.models;

import java.io.File;
import java.util.Objects;
//import java.util.InputMismatchException;

public abstract class Account {
    private final String directory;
    private String username;
    private String password;
    private String displayName;
    private String profilePicturePath;

    public Account(String username, String password, String displayName, String profilePicturePath) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePicturePath = profilePicturePath;

        directory = "data" + File.separator + username;
        createDirectory();
    }

    public Account(String username, String password, String displayName) {
        this(username, password, displayName, File.separator + "images" + File.separator + "profile.png");
    }

    public abstract void login() throws Exception;

    public abstract String getRole();

    public abstract String toCsv();

    private void createDirectory() {
        File directory = new File(getDirectory());
        if (!directory.exists())
            directory.mkdir();
    }

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    public boolean changePassword(String newPassword, String confirmNewPassword) {
        if (newPassword.equals(confirmNewPassword)) {
            setPassword(newPassword);
            return true;
        }
        return false;
    }

    public boolean changeDisplayName(String password, String confirmPassword, String newDisplayName) {
        if (password.equals(this.password))
            if (password.equals(confirmPassword)) {
                setDisplayName(newDisplayName);
                return true;
            }
        return false;
    }

    public void changeProfilePicturePath(String newPicturePath) {
        this.profilePicturePath = newPicturePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    //need authentication before use
    private void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    //need authentication before use
    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getDirectory() {
        return directory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return username.equals(account.username) && password.equals(account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

}
