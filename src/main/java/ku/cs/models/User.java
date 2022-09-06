package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User extends Account {

    protected boolean isBanned;
    private Shop shop;
    private LocalDateTime lastLogin;
    private int loginAttemptsAfterBanned;

    public User(String username, String password, String displayName, String profilePicturePath, String shopName, LocalDateTime lastLogin, boolean isBanned, int loginAttemptsAfterBanned) {
        super(username, password, displayName, profilePicturePath);

        this.setUpShop(shopName, null);
        this.lastLogin = lastLogin;
        this.isBanned = isBanned;
        this.loginAttemptsAfterBanned = loginAttemptsAfterBanned;
    }

    public Shop getShop() {
        return shop;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
//        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean set) {
        this.isBanned = set;
    }

    public int getLoginAttemptsAfterBanned() {
        return loginAttemptsAfterBanned;
    }

    public void setUpShop(String name, String pathShopImg) {
        this.setUpShop(name, new ProductList(), new OrderList(), pathShopImg);
    }

    public void setUpShop(String name, ProductList productList, OrderList orderList, String pathShopImg) {
        if (name == null || name.equals("null")) {
            return;
        }
        shop = new Shop(this, name, productList, orderList, pathShopImg);
    }

    @Override
    public void login() throws Exception {
        if (this.isBanned) {
            loginAttemptsAfterBanned++;
            throw new IllegalAccessException();
        }
        lastLogin = LocalDateTime.now();
    }

    @Override
    public String toCsv() {
        return "User, " +
                getUsername() + ", " +
                getPassword() + ", " +
                getDisplayName() + ", " +
                getProfilePicturePath() + ", " +
                (shop == null ? "null, " : shop.getName() + ", ") +
                (lastLogin == null ? "null, " : lastLogin + ", ") +
                isBanned + ", " +
                loginAttemptsAfterBanned;
    }

    @Override
    public String getRole() {
        return "User";
    }

    @Override
    public String toString() {
        if (this.lastLogin != null) {
            return getUsername() + " [" + lastLogin.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "]";
        } else {
            return getUsername() + " [ - ]";
        }
    }

    public void addLoginAttempt() {
        this.loginAttemptsAfterBanned++;
    }
}
