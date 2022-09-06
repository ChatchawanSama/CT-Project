package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.models.Shop;
import ku.cs.models.User;
import ku.cs.services.AccountListDataSource;
import ku.cs.services.ShopListDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class UserProfileController {

    @FXML
    private Button logoutButton;
    @FXML
    private Button goToMarketPlace;
    @FXML
    private Button uploadImage;
    @FXML
    private Label displayNameLabel;
    @FXML
    private Label displayNameLabel2;
    @FXML
    private Label displayUsername;
    @FXML
    private ImageView uploadImageIcon;
    @FXML
    private Label changeDisplayNameLabel;
    @FXML
    private Label changePasswordLabel;
    @FXML
    private Label changeImageLabel;
    @FXML
    private Pane marketplacePane;
    @FXML
    private Pane shopPane;
    @FXML
    private AnchorPane container;


    private Path imgDataPath;
    private boolean checkSelectPic;
    private Account account;
    @FXML
    private Rectangle profileRecImg;

    public void initialize() {
        account = (Account) FXRouter.getData();
        showAccountData();
        checkSelectPic = false;
        String pathAdminRoomIconImgView = getClass().getResource("/ku/cs/images/addImg.png").toExternalForm();
        uploadImageIcon.setImage(new Image(pathAdminRoomIconImgView));
//        String pathAddImgPic = getClass().getResource(account.getProfilePicturePath()).toExternalForm();
        //      addImgPicView.setImage(new Image(pathAddImgPic));

        shopPane.setVisible(!account.getRole().equals("Admin"));

        setupButton();

    }

    @FXML
    private void switchToLogin(ActionEvent event) {
        try {
            FXRouter.goToWithPosition("login", null, 302, 434, 600, 160);
        } catch (IOException var2) {
            System.out.println("Cannot go to login");
        }

    }

    @FXML
    private void switchToMarketPlace(ActionEvent event) {
        try {
            FXRouter.goTo("shop", new Object[]{null, account});
        } catch (IOException var2) {
            System.out.println("Cannot go to marketplace");
        }

    }

    @FXML
    private void switchToMyShop(ActionEvent event) {
        if (account.getRole().equals("User")) {
            User user = (User) account;
            if (user.getShop() == null) {
                try {
                    FXRouter.goTo("setUpShop", user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FXRouter.goTo("shop", new Object[]{user.getShop(), user});
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (account.getRole().equals("Admin")) {
            System.out.println("You are admin can't go to shop.");
        }
    }

    private void showAccountData() {
        File proFilePic = new File(account.getProfilePicturePath());
        Image proFilePicImg = new Image(proFilePic.toURI().toString());
        profileRecImg.setFill(new ImagePattern(proFilePicImg));
        displayNameLabel.setText(account.getDisplayName());
        displayNameLabel2.setText(account.getDisplayName());
        displayUsername.setText(account.getUsername());
    }


    @FXML
    private void handleUpLoadImage(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*tiff", "*raw"));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            checkSelectPic = true;
            try {
                File dataImgTempPng = new File("data" + File.separator + account.getUsername() + File.separator + selectedFile.getName());
                imgDataPath = FileSystems.getDefault().getPath(dataImgTempPng.getAbsolutePath());
                Files.copy(selectedFile.toPath(), imgDataPath, StandardCopyOption.REPLACE_EXISTING);
                File proFilePic = new File("data" + File.separator + account.getUsername() + File.separator + selectedFile.getName());
                Image proFilePicImg = new Image(proFilePic.toURI().toString());
                profileRecImg.setFill(new ImagePattern(proFilePicImg));
                //addImgPicView.setImage(proFilePicImg);

            } catch (IOException e) {
                //e.printStackTrace();
                System.err.println("Can't upload image.");
            }
            //System.out.println("Before Write");
            //System.out.println( account.getProfilePicturePath());

            String imgPath = "data" + File.separator + account.getUsername() + File.separator + selectedFile.getName();
            //System.out.println(imgPath);
            AccountListDataSource accountListDataSource = new AccountListDataSource();
            AccountList accountList = accountListDataSource.readData();
            accountList.searchByUsername(account.getUsername()).changeProfilePicturePath(imgPath);
            account.setProfilePicturePath(imgPath);
            //System.out.println("Check ------>"+imgPath);
            accountListDataSource.writeData(accountList);

            //System.out.println("After Write");
            //System.out.println( account.getProfilePicturePath());

        }
    }

    private void setupButton() {
        changeDisplayNameLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/ku/cs/changeDisplayName.fxml"));
                    Scene scene = changePasswordLabel.getScene();
                    root.translateXProperty().set(scene.getWidth());
                    AnchorPane parentContainer = (AnchorPane) scene.getRoot();
                    parentContainer.getChildren().add(root);

                    Timeline timeline = new Timeline();
                    KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                    KeyFrame kf = new KeyFrame(Duration.seconds(0.75), kv);
                    timeline.getKeyFrames().add(kf);
                    timeline.setOnFinished(event1 -> {
                        //parentContainer.getChildren().remove(container);
                        try {
                            FXRouter.goTo("changeDisplayName", account);
                        } catch (IOException var2) {
                            System.out.println("Cannot go to changeDisplayName");
                        }
                    });
                    timeline.play();
                } catch (IOException var2) {
                    System.out.println("Cannot load changeDisplayName.fxml");
                }
            }
        });

        setUnderlineWhenMouseEntered(changePasswordLabel);

        setUnderlineWhenMouseEntered(changeImageLabel);

        setUnderlineWhenMouseEntered(changeDisplayNameLabel);

        changePasswordLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/ku/cs/changePassword.fxml"));
                    Scene scene = changePasswordLabel.getScene();
                    root.translateXProperty().set(scene.getWidth());
                    AnchorPane parentContainer = (AnchorPane) scene.getRoot();
                    parentContainer.getChildren().add(root);

                    Timeline timeline = new Timeline();
                    KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                    KeyFrame kf = new KeyFrame(Duration.seconds(0.75), kv);
                    timeline.getKeyFrames().add(kf);
                    timeline.setOnFinished(event1 -> {
                        //parentContainer.getChildren().remove(container);
                        try {
                            FXRouter.goTo("changePassword", account);
                        } catch (IOException var2) {
                            System.out.println("Cannot go to changePassword");
                        }
                    });
                    timeline.play();
                    //FXRouter.goTo("changePassword", account);
                } catch (IOException var2) {
                    System.out.println("Cannot load changePassword.fxml");
                }
            }
        });

        changeImageLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*tiff", "*raw"));
                File selectedFile = fc.showOpenDialog(null);

                if (selectedFile != null) {
                    checkSelectPic = true;
                    try {
                        File dataImgTempPng = new File("data" + File.separator + account.getUsername() + File.separator + selectedFile.getName());
                        imgDataPath = FileSystems.getDefault().getPath(dataImgTempPng.getAbsolutePath());
                        Files.copy(selectedFile.toPath(), imgDataPath, StandardCopyOption.REPLACE_EXISTING);
                        File proFilePic = new File("data" + File.separator + account.getUsername() + File.separator + selectedFile.getName());
                        Image proFilePicImg = new Image(proFilePic.toURI().toString());
                        profileRecImg.setFill(new ImagePattern(proFilePicImg));
                        //addImgPicView.setImage(proFilePicImg);

                    } catch (IOException e) {
                        //e.printStackTrace();
                        System.err.println("Can't upload image.");
                    }
                    //System.out.println("Before Write");
                    //System.out.println( account.getProfilePicturePath());

                    String imgPath = "data" + File.separator + account.getUsername() + File.separator + selectedFile.getName();
                    //System.out.println(imgPath);
                    AccountListDataSource accountListDataSource = new AccountListDataSource();
                    AccountList accountList = accountListDataSource.readData();
                    accountList.searchByUsername(account.getUsername()).changeProfilePicturePath(imgPath);
                    account.setProfilePicturePath(imgPath);
                    //System.out.println("Check ------>"+imgPath);
                    accountListDataSource.writeData(accountList);

                    //System.out.println("After Write");
                    //System.out.println( account.getProfilePicturePath());

                }
            }
        });

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

    private void setUnderlineWhenMouseEntered(Label changePasswordLabel) {
        changePasswordLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                changePasswordLabel.setUnderline(true);
            }
        });

        changePasswordLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                changePasswordLabel.setUnderline(false);
            }
        });
    }


}
