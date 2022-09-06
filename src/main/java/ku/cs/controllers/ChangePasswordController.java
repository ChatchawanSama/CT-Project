package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.models.Shop;
import ku.cs.models.User;
import ku.cs.services.AccountListDataSource;
import ku.cs.services.ShopListDataSource;

import java.io.IOException;
import java.util.Scanner;

public class ChangePasswordController {
    private static Scanner x;
    //private User user;
    Account account;
    @FXML
    private Button goToMyProfile;
    @FXML
    private Button goToMarketPlace;
    @FXML
    private Label oldPasswordWarningLabel;
    @FXML
    private Label newPasswordWarningLabel;
    @FXML
    private Pane marketplacePane;
    @FXML
    private Pane shopPane;
    @FXML
    private AnchorPane container;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField passwordConfirmTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private TextField newPasswordConfirmTextField;
    @FXML
    private Button confirmChangePassword;

    public void initialize() {
        //user = (User) FXRouter.getData();
        account = (Account) FXRouter.getData();
        showAccountData();
        oldPasswordWarningLabel.setText("");
        newPasswordWarningLabel.setText("");
        shopPane.setVisible(!account.getRole().equals("Admin"));
        setupButton();
    }

    @FXML
    private void switchToMyProfile(ActionEvent event) {
        try {
            FXRouter.goTo("userProfile", this.account);
            //FXRouter.goTo("userProfile", this.user);
        } catch (IOException var2) {
            System.out.println("Cannot go to userProfile");
        }
    }

    @FXML
    private void switchToMarketPlace(ActionEvent event){
        try {
            FXRouter.goTo("shop", new Object[]{null, account});
        } catch (IOException var2) {
            System.out.println("Cannot go to marketplace");
        }
    }

    @FXML
    private void confirmChangePassword(ActionEvent event) {
        newPasswordWarningLabel.setText("");
        oldPasswordWarningLabel.setText("");
        String passwordStr = passwordTextField.getText();
        String passwordConfirmStr = passwordConfirmTextField.getText();
        String newPasswordStr = newPasswordTextField.getText();
        String newPasswordConfirmStr = newPasswordConfirmTextField.getText();

        if (account.checkPassword(passwordStr) && account.checkPassword(passwordConfirmStr)) {
            if (account.changePassword(newPasswordStr, newPasswordConfirmStr)) {
                if( newPasswordStr.length() > 5 ) {
                    if( !checkSpace(newPasswordStr) ) {
                        AccountListDataSource accountListDataSource = new AccountListDataSource();
                        AccountList accountList = accountListDataSource.readData();
                        accountList.searchByUsername((account.getUsername())).changePassword(newPasswordStr, newPasswordConfirmStr);
                        accountListDataSource.writeData(accountList);

                        try {
                            FXRouter.goTo("userProfile", this.account);
                            //FXRouter.goTo("userProfile", this.user);
                        } catch (IOException var2) {
                            System.out.println("Cannot go to userProfile");
                        }
                    }else{
                        newPasswordWarningLabel.setText("Password can't contain space.");
                        newPasswordWarningLabel.setTextFill(Color.rgb(255, 0, 0));
                    }
                }else{
                    newPasswordWarningLabel.setText("Password must have more than 5 characters.");
                    newPasswordWarningLabel.setTextFill(Color.rgb(255, 0, 0));
                }
            } else {
                newPasswordWarningLabel.setText("Invalid New Password");
                newPasswordWarningLabel.setTextFill(Color.rgb(255, 0, 0));
            }
        } else {
            oldPasswordWarningLabel.setText("Invalid Password");
            oldPasswordWarningLabel.setTextFill(Color.rgb(255, 0, 0));

        }

    }

    private boolean checkSpace(String str) {
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                return true;
            }
        }
        return false;
    }

    private void showAccountData() {
        //displayNameLabel.setText(account.getDisplayName());
        //roleLabel.setText(account.getRole());
        //displayUserName.setText(account.getUsername());
    }

    private void setupButton() {

        marketplacePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXRouter.goTo("shop", new Object[]{null, account});
                } catch (IOException var2) {
                    System.out.println("Cannot go to marketplace");
                }
            }
        });


        shopPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (account.getRole().equals("User")) {
                    Shop shop = ((User) account).getShop();
                    if (shop == null) {
                        try {
                            FXRouter.goTo("setUpShop", account);
                        } catch (IOException e) {
                            System.err.println("Cannot go to setUpShop");
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            shop = new ShopListDataSource().readData().searchShopFromName(shop.getName());
                            FXRouter.goTo("shop", new Object[]{shop, account});
                        } catch (IOException e) {
                            System.err.println("Cannot go to myShop");
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
