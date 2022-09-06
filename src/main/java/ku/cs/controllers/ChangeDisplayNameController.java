package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.models.Shop;
import ku.cs.models.User;
import ku.cs.services.AccountListDataSource;
import ku.cs.services.ShopListDataSource;

import java.io.IOException;

public class ChangeDisplayNameController {
    //private User user;
    Account account;
    @FXML
    private Button goToMyProfile;
    @FXML
    private Label displayNameLabel;
    @FXML
    private Label renameWarningLabel;
    @FXML
    private Label passwordWarningLabel;
    @FXML
    private Label passwordMatchWarningLabel;
    @FXML
    private Label marketplaceLabel;
    @FXML
    private Label myShopLabel;
    @FXML
    private ImageView marketplaceImg;
    @FXML
    private ImageView shopImg;
    @FXML
    private Pane marketplacePane;
    @FXML
    private Pane shopPane;
    @FXML
    private TextField displayNameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField passwordConfirmTextField;
    @FXML
    private Button confirmRename;

    public void initialize() {
        //user = (User) FXRouter.getData();
        account = (Account) FXRouter.getData();
        showAccountData();
        renameWarningLabel.setText("");
        passwordWarningLabel.setText("");
        passwordMatchWarningLabel.setText("");
        String pathMarketPlaceImgView = getClass().getResource("/ku/cs/images/shop.png").toExternalForm();
        marketplaceImg.setImage(new Image(pathMarketPlaceImgView));
        String pathShopImgView = getClass().getResource("/ku/cs/images/cart.png").toExternalForm();
        shopImg.setImage(new Image(pathShopImgView));
        shopPane.setVisible(!account.getRole().equals("Admin"));
        setupButton();

    }

    @FXML
    private void switchToMyProfile(ActionEvent event) {
        try {
            FXRouter.goTo("userProfile", this.account);
        } catch (IOException var2) {
            System.out.println("Cannot go to userProfile");
        }
    }

    @FXML
    private void switchToMarketPlace(ActionEvent event) {
        try {
            FXRouter.goTo("shop", new Object[]{null, account});
            //FXRouter.goTo("marketplace", this.user);

        } catch (IOException var2) {
            System.out.println("Cannot go to marketplace");
        }
    }

    @FXML
    private void confirmChangeDisplayName(ActionEvent event) {
        renameWarningLabel.setText("");
        String displayNameStr = displayNameTextField.getText();
        String passwordStr = passwordTextField.getText();
        String passwordConfirmStr = passwordConfirmTextField.getText();
        if( displayNameStr.length() > 5 ) {
            if (account.checkPassword(passwordStr)) {
                passwordWarningLabel.setText("");
                if (account.changePassword(passwordStr, passwordConfirmStr)) {
                    if (account.changeDisplayName(passwordStr, passwordConfirmStr, displayNameStr)) {
                        renameWarningLabel.setText("");
                        AccountListDataSource accountListDataSource = new AccountListDataSource();
                        AccountList accountList = accountListDataSource.readData();
                        accountList.searchByUsername(account.getUsername()).changeDisplayName(passwordStr, passwordConfirmStr, displayNameStr);
                        accountListDataSource.writeData(accountList);

                        try {
                            FXRouter.goTo("userProfile", this.account);
                            //FXRouter.goTo("userProfile", this.user);
                        } catch (IOException var2) {
                            System.out.println("Cannot go to userProfile");
                        }
                    } else {
                        renameWarningLabel.setText("Invalid name");
                        renameWarningLabel.setTextFill(Color.rgb(255, 0, 0));
                    }
                } else {
                    passwordMatchWarningLabel.setText("Invalid Password");
                    passwordMatchWarningLabel.setTextFill(Color.rgb(255, 0, 0));
                }
            } else {
                passwordWarningLabel.setText("Invalid Password");
                passwordWarningLabel.setTextFill(Color.rgb(255, 0, 0));
            }
        }else{
            renameWarningLabel.setText("Display name must have more than 5 characters.");
            renameWarningLabel.setTextFill(Color.rgb(255, 0, 0));
        }


    }


    private void showAccountData() {
        displayNameLabel.setText(account.getDisplayName());
        //displayNameLabel.setText(user.getDisplayName());
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
