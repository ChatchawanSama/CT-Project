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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import ku.cs.models.AccountList;
import ku.cs.models.ShopList;
import ku.cs.models.User;
import ku.cs.services.AccountListDataSource;
import ku.cs.services.ShopListDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SetUpShopController {

    User user = (User) FXRouter.getData();
    @FXML
    private TextField shopNameTextField;
    @FXML
    private Label alertSetUpShop;
    @FXML
    private Label shopImgPathLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button chooseShopImgButton;
    @FXML
    private ImageView createShopIconImgView;
    @FXML
    private ImageView createShopLabelImgView;
    @FXML
    private Rectangle shopProfileImgRec;
    private File selectedImg = null;
    private String pathShopImg;

    public void initialize() {
        setUpButton();
        alertSetUpShop.setVisible(false);
        shopImgPathLabel.setText("");
        String shopProfileImgPic = getClass().getResource("/ku/cs/images/default_shop.png").toExternalForm();
        shopProfileImgRec.setFill(new ImagePattern(new Image(shopProfileImgPic)));
        String pathCreateShopIconImgView = getClass().getResource("/ku/cs/images/create_shop_icon.png").toExternalForm();
        createShopIconImgView.setImage(new Image(pathCreateShopIconImgView));
        String pathCreateShopLabelImgView = getClass().getResource("/ku/cs/images/create_shop_label.png").toExternalForm();
        createShopLabelImgView.setImage(new Image(pathCreateShopLabelImgView));
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo("shop", new Object[]{null, user});
                } catch (IOException var2) {
                    System.out.println("Cannot go to marketplace");
                }
            }
        });
    }

    public void handleSubmitButton() {
        ShopListDataSource shopListDataSource = new ShopListDataSource();
        ShopList shopList = shopListDataSource.readData();

        if (shopNameTextField.getText().trim().isEmpty()) {
            alertSetUpShop.setText("Shop name cannot be empty.");
            alertSetUpShop.setVisible(true);
            return;
        } else if (shopList.searchShopFromName(shopNameTextField.getText().trim()) != null) {
            alertSetUpShop.setText("Shop name is duplicate.");
            alertSetUpShop.setVisible(true);
            return;
        }

        AccountListDataSource accountListDataSource = new AccountListDataSource();
        AccountList accountList = accountListDataSource.readData();
        user = ((User) accountList.searchByUsername(user.getUsername()));

        if (selectedImg != null) {
            File directory = new File(user.getDirectory() + File.separator + "shop");
            directory.mkdir();
            File tempPicFile = new File("data" + File.separator + "temp.png");
            File renameTempPicFile = new File(directory + File.separator + shopNameTextField.getText().trim() + ".png");
            tempPicFile.renameTo(renameTempPicFile);
            pathShopImg = directory + File.separator + shopNameTextField.getText().trim() + ".png";
        } else pathShopImg = "data" + File.separator + "default_images" + File.separator + "profile.png";

        ((User) accountList.searchByUsername(user.getUsername())).setUpShop(shopNameTextField.getText().trim(), pathShopImg);
        accountListDataSource.writeData(accountList);

        shopList.add(user.getShop());
        shopListDataSource.writeData(shopList);

        try {
            FXRouter.goTo("shop", new Object[]{user.getShop(), user});
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot go to shop from setUpShop.");
        }
    }

    private void setUpButton() {
        chooseShopImgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser imgChooser = new FileChooser();
                imgChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG JPG File", "*.png", "*.jpg"));
                selectedImg = imgChooser.showOpenDialog(null);
                if (selectedImg != null) {
                    try {
                        shopImgPathLabel.setText(selectedImg.getPath());
                        File dataImgTempPng = new File("data" + File.separator + "temp.png");
                        Path imgDataPath = FileSystems.getDefault().getPath(dataImgTempPng.getAbsolutePath());
                        Files.copy(selectedImg.toPath(), imgDataPath, StandardCopyOption.REPLACE_EXISTING);
                        shopProfileImgRec.setFill(new ImagePattern(new Image(dataImgTempPng.toURI().toString())));
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Can't upload image.");
                    }
                }
            }
        });
    }
}
