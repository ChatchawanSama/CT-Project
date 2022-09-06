package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.models.User;
import ku.cs.services.AccountListDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class RegisterController {


    @FXML
    private ImageView registerIconImgView;
    @FXML
    private ImageView addImgPicView;
    @FXML
    private ImageView registrationLabelPicImgView;
    @FXML
    private Button registerButton;
    @FXML
    private Button returnToLogin;
    @FXML
    private Button chooseImgButton;
    @FXML
    private TextField displayNameTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passWordTextField;
    @FXML
    private TextField conFirmPassWordTextField;
    @FXML
    private Label warningRegisterLabel;
    @FXML
    private Label pathImgLabel;

    private File selectedImg = null;
    private Account account;
    private Path imgDataPath;
    private String pathUserImg;

    public void initialize() {
        String pathRegisterIconImgView = getClass().getResource("/ku/cs/images/registration_icon.png").toExternalForm();
        registerIconImgView.setImage(new Image(pathRegisterIconImgView));
        String pathRegistrationLabelPicImgView = getClass().getResource("/ku/cs/images/registration_text.png").toExternalForm();
        registrationLabelPicImgView.setImage(new Image(pathRegistrationLabelPicImgView));
        warningRegisterLabel.setText("");
        pathImgLabel.setText("");
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {
        String displayNameStr = displayNameTextField.getText();
        String userNameStr = userNameTextField.getText();
        String passWordStr = passWordTextField.getText();

        AccountListDataSource accountListDataSource = new AccountListDataSource();
        AccountList accountList = accountListDataSource.readData();
        account = accountList.searchByUsername(userNameStr);
        if (checkRegister()) {
            account = new User(userNameStr, passWordStr, displayNameStr, pathUserImg, null, null, false, 0);
            if (selectedImg != null) {
                File tempPicFile = new File("data" + File.separator + "temp.png");
                try {
                    Files.createDirectories(Paths.get("data" + File.separator + userNameStr + File.separator));
                } catch (IOException e) {
                    System.out.println("Cannot create directories.");
                    e.printStackTrace();
                }
                File renameTempPicFile = new File("data" + File.separator + userNameStr + File.separator + userNameStr + ".png");
                tempPicFile.renameTo(renameTempPicFile);
                pathUserImg = "data" + File.separator + userNameStr + File.separator + userNameStr + ".png";
            } else pathUserImg = "data" + File.separator + "default_images" + File.separator + "profile.png";
            account.setProfilePicturePath(pathUserImg);
            accountList.add(account);
            accountListDataSource.writeData(accountList);
            try {
                FXRouter.goTo("login", null, 302, 434);
            } catch (IOException e) {
                System.out.println("Cannot go to Login");
                e.printStackTrace();
            }
        }
    }

    private boolean checkRegister() {
        String displayNameStr = displayNameTextField.getText();
        String userNameStr = userNameTextField.getText();
        String passWordStr = passWordTextField.getText();
        String confirmPasswordStr = conFirmPassWordTextField.getText();

        if (account != null) {
            warningRegisterLabel.setText("Your username is duplicate.");
            return false;
        } else if (displayNameStr.length() < 6) {
            warningRegisterLabel.setText("Display name must have more than 5 characters.");
            return false;
        } else if (checkStringIsAlNum(userNameStr)) {
            warningRegisterLabel.setText("Username must contain only A-Z,a-z and 0-9.");
            return false;
        } else if (userNameStr.length() < 6) {
            warningRegisterLabel.setText("Username must have more than 5 characters.");
            return false;
        } else if (passWordStr.length() < 6) {
            warningRegisterLabel.setText("Password must have more than 5 characters.");
            return false;
        } else if (checkSpace(passWordStr)) {
            warningRegisterLabel.setText("Password can't contain space.");
            return false;
        } else if (!passWordStr.equals(confirmPasswordStr)) {
            warningRegisterLabel.setText("Confirm Password is Incorrect.");
            return false;
        }
        return true;
    }

    private boolean checkSpace(String str) {
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                return true;
            }
        }
        return false;
    }

    private boolean checkStringIsAlNum(String str) {
        for (char c : str.toCharArray()) {
            if (!(Character.isDigit(c) || Character.isLetter(c))) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void handleChooseImgButton(ActionEvent event) {
        FileChooser imgChooser = new FileChooser();
        imgChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG JPG Files", "*.png", "*.jpg"));
        selectedImg = imgChooser.showOpenDialog(null);
        if (selectedImg != null) {
            try {
                pathImgLabel.setText(selectedImg.getPath());
                File dataImgTempPng = new File("data" + File.separator + "temp.png");
                imgDataPath = FileSystems.getDefault().getPath(dataImgTempPng.getAbsolutePath());
                Files.copy(selectedImg.toPath(), imgDataPath, StandardCopyOption.REPLACE_EXISTING);
                addImgPicView.setImage(new Image(dataImgTempPng.toURI().toString()));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Can't upload image.");
                warningRegisterLabel.setText("Can't upload image.");
            }
        }
    }

    @FXML
    private void switchToLogin(ActionEvent event) {
        try {
            FXRouter.goTo("login", null, 302, 434);
        } catch (IOException e) {
            System.out.println("Cannot go to Login");
            e.printStackTrace();
        }
    }

    @FXML
    private void mouseEnteredTheRegisterButton(MouseEvent mouseEvent) {
        registerButton.setStyle("-fx-background-color: #CC3333;");
    }

    @FXML
    private void mouseExitedTheRegisterButton(MouseEvent mouseEvent) {
        registerButton.setStyle("-fx-background-color: #FF6666;");
    }

    @FXML
    private void mouseEnteredTheBackButton(MouseEvent mouseEvent) {
        returnToLogin.setStyle("-fx-border-width: 1px; -fx-border-color: #BEBEBE; -fx-background-color: #CCCCCC;");
    }

    @FXML
    private void mouseExitedTheBackButton(MouseEvent mouseEvent) {
        returnToLogin.setStyle("-fx-border-width: 1px; -fx-border-color: #BEBEBE; -fx-background-color: #F5F5F5;");
    }

}
