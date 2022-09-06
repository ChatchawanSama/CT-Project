package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class AccountList {
    private final List<Account> accounts;

    public AccountList() {
        accounts = new ArrayList<>();
    }

    public void add(Account account) {
        accounts.add(account);
    }

    public Account searchByUsername(String username) {
        for (Account account : accounts)
            if (username.equalsIgnoreCase(account.getUsername()))
                return account;

        return null;
    }

    public String toCsv() {
        String result = "";

        for (Account account : accounts)
            result += account.toCsv() + "\n";

        return result;
    }
}
