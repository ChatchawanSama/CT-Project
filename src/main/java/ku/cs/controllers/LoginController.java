package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ku.cs.App;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.models.User;
import ku.cs.services.AccountListDataSource;

import java.awt.*;
import java.io.File;
import java.io.IOException;


public class LoginController {

    @FXML
    private Button loginToRegister;
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private ImageView ctLoginLogoImgView;
    @FXML
    private ImageView userIconImgView;
    @FXML
    private ImageView passwordIconImgView;
    @FXML
    private Label loginWarningLabel;
    @FXML
    private Label creditLabel;
    @FXML
    private AnchorPane loginGP;
    private Account account;

    public void initialize() {

        String pathLoginLogo = getClass().getResource("/ku/cs/images/login_logo.png").toExternalForm();
        ctLoginLogoImgView.setImage(new Image(pathLoginLogo));
        String pathUserIconImgView = getClass().getResource("/ku/cs/images/account_icon.png").toExternalForm();
        userIconImgView.setImage(new Image(pathUserIconImgView));
        String pathPasswordIconImgView = getClass().getResource("/ku/cs/images/password_icon.png").toExternalForm();
        passwordIconImgView.setImage(new Image(pathPasswordIconImgView));
        loginWarningLabel.setText("");
        String lightModePath = getClass().getResource("/ku/cs/css/modena/lightMode.css").toExternalForm();
        App.setUserAgentStylesheet(lightModePath);

    }

    @FXML
    private void showCreditForm(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/credit.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            System.out.println("Cannot load Scene");
            e.printStackTrace();
        }

        String pathLogo = getClass().getResource("/ku/cs/images/logo.png").toExternalForm();
        stage.getIcons().add(new Image(pathLogo));
        stage.setTitle("Developer");
        stage.show();
    }

    @FXML
    private void switchToRegisterButtonOnAction(ActionEvent event) {
        try {
            FXRouter.goTo("register", null, 371, 485);
        } catch (IOException e) {
            System.out.println("Cannot go to register from Login.");
            e.printStackTrace();
        }
    }

    private void makeFadeOut(String s) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(loginGP);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished((ActionEvent event) -> {
            if (s.equals("admin")) {
                try {
                    FXRouter.goToWithPosition("shop", new Object[]{null, account}, 300, 15);
                } catch (IOException e) {
                    System.err.println("Can't go to marketplace by admin");
                }
            } else if (s.equals("user")) {
                try {
                    FXRouter.goToWithPosition("shop", new Object[]{null, account}, 300, 15);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        fadeTransition.play();
    }

    @FXML
    private void goToMarketPlace(ActionEvent event) {
        String userNameStr = usernameTextField.getText();
        String passwordStr = passwordTextField.getText();
        AccountListDataSource dataSource = new AccountListDataSource();
        AccountList accountList = dataSource.readData();
        account = accountList.searchByUsername(userNameStr);
        if (account != null) {
            if (account.getPassword().equals(passwordStr)) {
                try {
                    account.login();
                } catch (Exception e) {
                    loginWarningLabel.setText("You have been banned.");
                    e.printStackTrace();
                }
                dataSource.writeData(accountList);

                if (account.getRole().equals("Admin")) {
                    makeFadeOut("admin");
                }
                if (account.getRole().equals("User")) {
                    if (!((User) account).isBanned()) {
                        makeFadeOut("user");

                    } else
                        loginWarningLabel.setText("You have been banned.");
                }
            } else
                loginWarningLabel.setText("Invalid Password.");
        } else
            loginWarningLabel.setText("Invalid Username.");
    }

    @FXML
    private void openManual(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().open(new File("data" + File.separator + "Manual.pdf"));
        } catch (IOException e) {
            System.out.println("Cannot open manual.pdf");
            e.printStackTrace();
        }
    }

    @FXML
    private void mouseEnteredTheLoginButton(MouseEvent mouseEvent) {
        loginButton.setStyle("-fx-background-color: #CC3333;");
    }

    @FXML
    private void mouseExitedTheLoginButton(MouseEvent mouseEvent) {
        loginButton.setStyle("-fx-background-color: #FF6666;");
    }

    @FXML
    private void mouseEnteredTheRegisterButton(MouseEvent mouseEvent) {
        loginToRegister.setStyle("-fx-border-width: 1px; -fx-border-color: #BEBEBE; -fx-background-color: #CCCCCC;");
    }

    @FXML
    private void mouseExitedTheRegisterButton(MouseEvent mouseEvent) {
        loginToRegister.setStyle("-fx-border-width: 1px; -fx-border-color: #BEBEBE; -fx-background-color: #F5F5F5;");
    }

}
